package com.ezscrum.microservice.productbacklog.Controllers;

import com.ezscrum.microservice.productbacklog.Entities.Story;
import com.ezscrum.microservice.productbacklog.Entities.Tag;
import com.ezscrum.microservice.productbacklog.Services.Tag.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tag")
public class TagController {
    TagServiceImpl tagService;

    @Autowired
    public TagController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public Tag create(@Valid @RequestBody Tag tag) {
        return tagService.create(tag);
    }

    @PutMapping("/update/{id}")
    public Tag update(@PathVariable(value = "id") Long id, @Valid @RequestBody Tag tag) {
        return tagService.update(id, tag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        return tagService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.unprocessableEntity().build();
    }
}
