package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.LocationDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Location;
import de.uniwue.dachs.fotolyrik_backend.repository.LocationRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LocationMapper {
    private final LocationRepository locationRepository;

    public LocationMapper(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location LocationDTOToLocation(LocationDTO locationDTO) {
        if (locationDTO == null) return null;
        if (locationDTO.getId() != null) {
            return locationRepository.findById(locationDTO.getId()).orElse(null);
        } else {
            Location location = new Location();
            location.setName(locationDTO.getName());
            return locationRepository.save(location);
        }
    }

    public Set<Location> LocationDTOsToLocations(Set<LocationDTO> locationDTOs) {
        if (locationDTOs == null || locationDTOs.isEmpty()) return Collections.emptySet();
        return locationDTOs.stream()
                .map(this::LocationDTOToLocation)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public LocationDTO LocationToLocationDTO(Location location) {
        if (location == null) return null;
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(location.getId());
        locationDTO.setName(location.getName());
        return locationDTO;
    }

    public Set<LocationDTO> LocationsToLocationDTOs(Set<Location> locations) {
        if (locations == null || locations.isEmpty()) return Collections.emptySet();
        return locations.stream()
                .map(this::LocationToLocationDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
