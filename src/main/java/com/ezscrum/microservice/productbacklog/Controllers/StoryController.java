package com.ezscrum.microservice.productbacklog.Controllers;

import com.ezscrum.microservice.productbacklog.Entities.Project;
import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Exceptions.ValidationErrorBuilder;
import com.ezscrum.microservice.productbacklog.Services.Story.StoryServiceImpl;
import com.ezscrum.microservice.productbacklog.Support.Filter.FilterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/story")
public class StoryController {
    StoryServiceImpl storyService;

    @Autowired
    public StoryController(StoryServiceImpl storyService) {
        this.storyService = storyService;
    }


    @PostMapping("/create")
    public ResponseEntity create(@Valid @RequestBody Story story, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        Story createdStory = storyService.create(story);
        return ResponseEntity.ok(createdStory);
    }

    @GetMapping("/get/{id}")
    public Story get(@PathVariable(value = "id") Long id) {
        return storyService.get(id);
    }

    @GetMapping("/{id}/tags")
    public Set<Tag> getTags(@PathVariable(value = "id") Long id) {
        return storyService.getTags(id);
    }

    @PutMapping("/update/{id}")
    public Story update(@PathVariable(value = "id") Long id, @Valid @RequestBody Story story) {
        return storyService.update(id, story);
    }

    @GetMapping("{id}/attach_tag/{tagId}")
    public Story attachTags(@PathVariable(value = "id") Long id, @PathVariable(value = "tagId") Long tagId){
        return storyService.attachTag(id, tagId);
    }

    @GetMapping("{id}/un_attach_tag/{tagId}")
    public Boolean unAttachTags(@PathVariable(value = "id") Long id, @PathVariable(value = "tagId") Long tagId){
        return storyService.unAttachTag(id, tagId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        return storyService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.unprocessableEntity().build();
    }
}
