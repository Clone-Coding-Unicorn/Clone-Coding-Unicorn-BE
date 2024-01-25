package com.sparta.clonecodingunicorn.domain.profile.controller;

import com.sparta.clonecodingunicorn.domain.member.dto.MemberResponseDto;
import com.sparta.clonecodingunicorn.domain.profile.dto.UpdateRequestDto;
import com.sparta.clonecodingunicorn.domain.profile.service.ProfileService;
import com.sparta.clonecodingunicorn.global.security.MemberDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/mypage")
    public ResponseEntity<MemberResponseDto> getMyPage(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return profileService.getMyPage(memberDetails);
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> getProfile(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return profileService.getProfile(memberDetails);
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateMember(@RequestBody UpdateRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return profileService.updateMember(requestDto, memberDetails);
    }

    // Request Param 방식의 프로필 수정 기능
    @PatchMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer birthYear,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String job,
            @RequestParam(required = false) String interestArea,
            @RequestParam(required = false) Boolean deleteMember,
            @RequestParam(required = false) Boolean subscribeAgree,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        UpdateRequestDto requestDto = UpdateRequestDto.builder()
                .name(name)
                .birthYear(birthYear)
                .gender(gender)
                .job(job)
                .interestArea(interestArea)
                .deleteMember(deleteMember)
                .subscribeAgree(subscribeAgree)
                .build();

        return profileService.updateProfile(requestDto, memberDetails);
    }
}
