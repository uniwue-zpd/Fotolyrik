package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.LocationDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Location;
import de.uniwue.dachs.fotolyrik_backend.repository.LocationRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.LocationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    public List<LocationDTO> getAllLocations() {
        return locationMapper.LocationsToLocationDTOs(locationRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    public Optional<LocationDTO> getLocationById(Long id) {
        return locationRepository.findById(id).map(locationMapper::LocationToLocationDTO);
    }

    @Transactional
    public LocationDTO createLocation(LocationDTO locationDTO) {
        var entity = locationMapper.LocationDTOToLocation(locationDTO);
        var savedEntity = locationRepository.save(entity);
        return locationMapper.LocationToLocationDTO(savedEntity);
    }

    @Transactional
    public LocationDTO updateLocation(Long id, LocationDTO updatedLocationDTO) {
        return locationRepository.findById(id)
                .map(existingLocation -> {
                    existingLocation.setName(updatedLocationDTO.getName());
                    existingLocation.setDescription(updatedLocationDTO.getDescription());
                    return locationRepository.save(existingLocation);
                }).map(locationMapper::LocationToLocationDTO)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location with ID '" + id + "' does not exist");
        }
        locationRepository.deleteById(id);
    }
}
