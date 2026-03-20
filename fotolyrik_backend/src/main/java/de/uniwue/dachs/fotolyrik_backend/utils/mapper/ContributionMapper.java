package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.ContributionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Contribution;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.ContributionRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ContributionMapper {
    private final ContributionRepository contributionRepository;
    private final PersonMapper personMapper;

    public ContributionMapper(ContributionRepository contributionRepository, PersonMapper personMapper) {
        this.contributionRepository = contributionRepository;
        this.personMapper = personMapper;
    }
    public Contribution ContributionDTOToContribution(ContributionDTO contributionDTO, Photopoem contributedTo){
        Consumer<Contribution> updateContribution = (contribution)->{
            contribution.setRole(contributionDTO.getRole());
            contribution.setContributor(personMapper.PreviewDTOToPerson( contributionDTO.getContributor()));
            contribution.setPseudonym(contributionDTO.getPseudonym());
            contribution.setWorkContributedTo(contributedTo);
        };

        if (contributionDTO == null) throw new RuntimeException("No Contribution DTO provided");
        if (contributionDTO.getId() == null) {
            // create new contribution in db
            var contribution = new Contribution();
            updateContribution.accept(contribution);
            return contribution;
        }else{
            var contribution = contributionRepository.findById(contributionDTO.getId()).orElse(null);
            if (contribution == null) throw new RuntimeException("Contribution id not found");
            updateContribution.accept(contribution);
            return contribution;
        }
    }
    public Set<Contribution> ContributionDTOsToContributions(Set<ContributionDTO> contributionDTOs, Photopoem contributedTo){
        if (contributionDTOs.isEmpty()) return Collections.emptySet();
        return contributionDTOs.stream()
                .map(dto -> ContributionDTOToContribution(dto, contributedTo))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    public ContributionDTO ContributionToContributionDTO(Contribution contribution){
        if (contribution == null) return null;
        var contributionDTO = new ContributionDTO();
        contributionDTO.setId(contribution.getId());
        contributionDTO.setContributor(personMapper.PersonToPreviewDTO(contribution.getContributor()));
        contributionDTO.setRole(contribution.getRole());
        contributionDTO.setPseudonym(contribution.getPseudonym());
        return contributionDTO;
    }
    public Set<ContributionDTO> ContributionsToContributionDTOs(Set<Contribution> contributions){
        if (contributions.isEmpty()) return Collections.emptySet();
        return contributions.stream()
                .map(this::ContributionToContributionDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
