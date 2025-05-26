package com.example.surveyapp.Controller;

import com.example.surveyapp.Entity.Option;
import com.example.surveyapp.Service.OptionService;
import com.example.surveyapp.dto.CreateOptionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<Option> addOption(@PathVariable Long questionId, @RequestBody CreateOptionRequest createOptionRequest) {
        Option created = optionService.addOption(questionId, createOptionRequest);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/questions/{questionId}/options")
    public ResponseEntity<List<Option>> getOptions(@PathVariable Long questionId) {
        List<Option> options = optionService.getOptionsForQuestion(questionId);
        return ResponseEntity.ok(options);
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<Option> updateOption(
            @PathVariable Long optionId,
            @RequestBody CreateOptionRequest request
    ) {
        Option updated = optionService.updateOption(optionId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
