package com.twenty.inhub.boundedContext.community.controller;

import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.community.repository.CommunityRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/communitiy")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityRepository communityRepository;

    @PostMapping
    public Community createCommunity(@RequestBody Community community) {
        return communityRepository.save(community);
    }

    @GetMapping("/{id}")
    public Community getCommunityById(@PathVariable Long id) {
        return communityRepository.findById(id)
                .orElseThrow(EntityExistsException::new);
    }

    @PutMapping("/{id}")
    public Community updateCommunity(@PathVariable Long id, @RequestBody Community updatedCommunity) {
        Community existingCommunity = communityRepository.findById(id)
                .orElseThrow(EntityExistsException::new);
        existingCommunity.setTitle(updatedCommunity.getTitle());
        existingCommunity.setContent(updatedCommunity.getContent());
        return communityRepository.save(existingCommunity);
    }

    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(EntityExistsException::new);
        communityRepository.delete(community);
    }
}