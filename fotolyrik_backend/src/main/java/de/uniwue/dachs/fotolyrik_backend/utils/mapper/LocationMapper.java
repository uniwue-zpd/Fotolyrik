package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.LocationDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Location;
import de.uniwue.dachs.fotolyrik_backend.repository.LocationRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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

    public LocationDTO LocationToLocationDTO(Location location) {
        if (location == null) return null;
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(location.getId());
        locationDTO.setName(location.getName());
        locationDTO.setBaseEntityFields(location);
        return locationDTO;
    }

    public Set<Location> LocationDTOsToLocations(Set<LocationDTO> locationDTOs) {
        return MapperUtils.mapSet(locationDTOs, this::LocationDTOToLocation);
    }
    public Set<LocationDTO> LocationsToLocationDTOs(Set<Location> locations) {
        return MapperUtils.mapSet(locations, this::LocationToLocationDTO);
    }
    public List<Location> LocationDTOsToLocations(List<LocationDTO> locationDTOs) {
        return MapperUtils.mapList(locationDTOs, this::LocationDTOToLocation);
    }
    public List<LocationDTO> LocationsToLocationDTOs(List<Location> locations) {
        return MapperUtils.mapList(locations, this::LocationToLocationDTO);
    }
}
