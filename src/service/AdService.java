package service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avito.dto.AdDto;
import ru.avito.dto.CreateOrUpdateAdDto;
import ru.avito.entity.Ad;
import ru.avito.entity.User;
import ru.avito.mapper.AdMapper;
import ru.avito.repository.AdRepository;
import ru.avito.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @Transactional(readOnly = true)
    public List<AdDto> getAllAds() {
        return adRepository.findAll().stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdDto getAdById(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        return adMapper.toDto(ad);
    }

    @Transactional
    public AdDto createAd(CreateOrUpdateAdDto createAdDto, String userEmail) {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Ad ad = adMapper.toEntity(createAdDto);
        ad.setAuthor(author);
        Ad savedAd = adRepository.save(ad);
        return adMapper.toDto(savedAd);
    }

    @Transactional
    public AdDto updateAd(Long id, CreateOrUpdateAdDto updateAdDto, String userEmail) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        if (!ad.getAuthor().getEmail().equals(userEmail) && !isAdmin(userEmail)) {
            throw new AccessDeniedException("You can't update this ad");
        }
        adMapper.updateAdFromDto(updateAdDto, ad);
        Ad updatedAd = adRepository.save(ad);
        return adMapper.toDto(updatedAd);
    }

    @Transactional
    public void deleteAd(Long id, String userEmail) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        if (!ad.getAuthor().getEmail().equals(userEmail) && !isAdmin(userEmail)) {
            throw new AccessDeniedException("You can't delete this ad");
        }
        adRepository.delete(ad);
    }

    private boolean isAdmin(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole() == Role.ADMIN;
    }
}