package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemSelectionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PhotopoemSelectionRequest;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelection;
import de.uniwue.dachs.fotolyrik_backend.model.PhotopoemSelectionType;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemSelectionRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.enums.AccessLevel;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PhotopoemSelectionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PhotopoemSelectionService {
    private final PhotopoemSelectionRepository photopoemSelectionRepository;
    private final PhotopoemRepository photopoemRepository;
    private final PhotopoemSelectionMapper photopoemSelectionMapper;

    public PhotopoemSelectionService(PhotopoemSelectionRepository photopoemSelectionRepository,
            PhotopoemRepository photopoemRepository,
            PhotopoemSelectionMapper photopoemSelectionMapper) {
        this.photopoemSelectionRepository = photopoemSelectionRepository;
        this.photopoemRepository = photopoemRepository;
        this.photopoemSelectionMapper = photopoemSelectionMapper;
    }

    /**
     * GET all photopoem selections ordered by ID.
     * @return a {@link List} of {@link PhotopoemSelectionDTO} objects
     */
    @Transactional(readOnly = true)
    public List<PhotopoemSelectionDTO> getAllPhotopoemSelections() {
        return photopoemSelectionRepository.findAll()
                .stream()
                .map(photopoemSelectionMapper::PhotopoemSelectionToPhotopoemSelectionDTO)
                .sorted(Comparator.comparing(PhotopoemSelectionDTO::getId)).toList();
    }

    /**
     * GET photopoem selections of the given type ordered by ID.
     * @param type the selection type to find
     * @return a {@link List} of matching {@link PhotopoemSelectionDTO} objects
     */
    @Transactional(readOnly = true)
    public List<PhotopoemSelectionDTO> getPhotopoemSelectionsByType(PhotopoemSelectionType type) {
        return photopoemSelectionRepository.findAllByTypeOrderByIdAsc(type)
                .stream()
                .map(photopoemSelectionMapper::PhotopoemSelectionToPhotopoemSelectionDTO)
                .toList();
    }

    /**
     * GET a photopoem selection by its ID.
     * @param id ID of the selection
     * @return an {@link Optional} containing the selection, or empty if it does not exist
     */
    @Transactional(readOnly = true)
    public Optional<PhotopoemSelectionDTO> getPhotopoemSelectionById(Long id) {
        return photopoemSelectionRepository.findById(id)
                .map(photopoemSelectionMapper::PhotopoemSelectionToPhotopoemSelectionDTO);
    }

    /**
     * Create a selection for a photopoem.
     * @param request the photopoem ID and selection type
     * @return the persisted selection as a {@link PhotopoemSelectionDTO}
     * @throws ResponseStatusException if the request is invalid, the photopoem is missing or already selected
     */
    @Transactional
    public PhotopoemSelectionDTO createPhotopoemSelection(PhotopoemSelectionRequest request) {
        validatePhotopoemSelectionRequest(request);
        Photopoem photopoem = findEligiblePhotopoemForSelection(request);
        if (photopoemSelectionRepository.existsByPhotopoem_Id(photopoem.getId())) {
            throw duplicatePhotopoemSelection();
        }
        PhotopoemSelection selection = new PhotopoemSelection();
        selection.setPhotopoem(photopoem);
        selection.setType(request.type());
        PhotopoemSelection savedSelection = photopoemSelectionRepository.save(selection);
        return photopoemSelectionMapper.PhotopoemSelectionToPhotopoemSelectionDTO(savedSelection);
    }

    /**
     * Update a selection's photopoem and type.
     * @param id ID of the selection to update
     * @param request the new photopoem ID and selection type
     * @return the updated selection as a {@link PhotopoemSelectionDTO}
     * @throws ResponseStatusException if the request is invalid, a resource is missing or the photopoem is already selected
     */
    @Transactional
    public PhotopoemSelectionDTO updatePhotopoemSelection(Long id, PhotopoemSelectionRequest request) {
        validatePhotopoemSelectionRequest(request);
        return photopoemSelectionRepository.findByIdForUpdate(id)
                .map(selection -> {
                    Photopoem photopoem = findEligiblePhotopoemForSelection(request);
                    if (photopoemSelectionRepository.existsByPhotopoem_IdAndIdNot(photopoem.getId(), id)) {
                        throw duplicatePhotopoemSelection();
                    }
                    selection.setPhotopoem(photopoem);
                    selection.setType(request.type());
                    PhotopoemSelection savedSelection = photopoemSelectionRepository.save(selection);
                    return photopoemSelectionMapper.PhotopoemSelectionToPhotopoemSelectionDTO(savedSelection);
                })
                .orElseThrow(() -> photopoemSelectionNotFound(id));
    }

    /**
     * Delete a photopoem selection by its ID.
     * @param id ID of the selection to delete
     * @throws ResponseStatusException if the selection does not exist
     */
    @Transactional
    public void deletePhotopoemSelection(Long id) {
        PhotopoemSelection selection = photopoemSelectionRepository.findByIdForUpdate(id)
                .orElseThrow(() -> photopoemSelectionNotFound(id));
        photopoemSelectionRepository.delete(selection);
    }

    /**
     * Check that a selection request contains a valid photopoem ID and type.
     * @param request the request to validate
     * @throws ResponseStatusException if required values are missing or invalid
     */
    private void validatePhotopoemSelectionRequest(PhotopoemSelectionRequest request) {
        if (request == null || request.photopoemId() == null || request.photopoemId() <= 0 || request.type() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A positive photopoemId and type are required");
        }
    }

    /**
     * Find the requested photopoem and check carousel image eligibility.
     * @param request the validated selection request
     * @return the locked photopoem
     * @throws ResponseStatusException if the photopoem is missing or ineligible for the carousel
     */
    private Photopoem findEligiblePhotopoemForSelection(PhotopoemSelectionRequest request) {
        Photopoem photopoem = photopoemRepository.findByIdForSelection(request.photopoemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photopoem not found"));
        if (request.type() == PhotopoemSelectionType.CAROUSEL
                && (photopoem.getImagesVisible() != AccessLevel.PUBLIC || photopoem.getImages().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A carousel photopoem must have at least one attached image and public image visibility");
        }
        return photopoem;
    }

    /**
     * @param id ID of the missing selection
     * @return a not-found exception for the selection
     */
    private ResponseStatusException photopoemSelectionNotFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Photopoem selection not found: " + id);
    }

    /**
     * @return a conflict exception for a photopoem that is already selected
     */
    private ResponseStatusException duplicatePhotopoemSelection() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Photopoem already has a selection");
    }
}
