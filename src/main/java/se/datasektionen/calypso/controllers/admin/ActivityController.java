package se.datasektionen.calypso.controllers.admin;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import se.datasektionen.calypso.auth.DAuthUserDetails;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;
import se.datasektionen.calypso.models.entities.Activity;
import se.datasektionen.calypso.models.repositories.ActivityRepository;
import se.datasektionen.calypso.Darkmode;

@Controller
@PreAuthorize("hasAuthority('post')")
@RequestMapping("/admin/activities")
@RequiredArgsConstructor
public class ActivityController {

    private static final int PAGE_SIZE = 50;

    private final ActivityRepository activityRepository;
    private final Darkmode darkmode;
    private final DateTimeFormatter formatter;

    private String showEditForm(Activity activity, Model model) {
        model.addAttribute("activity", activity);
        model.addAttribute("formatter", formatter);

        return "activities/edit";
    }

    @GetMapping("/new")
    public String newForm(Authentication auth, Model model) {
        var user = (DAuthUserDetails) auth.getPrincipal();

        var activity = new Activity();
        activity.setAuthor(user.getUser());
        activity.setAuthorDisplay(user.getName());

        return this.showEditForm(activity, model);
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable long id, Model model) {
        var activity = activityRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        return this.showEditForm(activity, model);
    }

    @PostMapping("/edit")
    public String doEdit(@Valid Activity activity, Authentication auth, BindingResult bindingResult) {
        // check for form errors
        if (bindingResult.hasErrors()) {
            return "activities/edit";
        }

        var user = (DAuthUserDetails) auth.getPrincipal();
        if (!user.isEditor()) {
            // prevent spoofing
            activity.setAuthor(user.getUser());
            activity.setAuthorDisplay(user.getName());
        }

        activity = activityRepository.save(activity);

        return "redirect:/admin/activities/edit/" + activity.getId() + "?saved=true";
    }

    @PreAuthorize("hasAuthority('editor')")
    @PostMapping("/delete/{id}")
    public String doDelete(@PathVariable long id) {
        activityRepository.deleteById(id);
        return "redirect:/admin/activities/list?deleted=" + id;
    }

    @RequestMapping("/list")
    public String list(@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sort", defaultValue = "DESC") Sort.Direction sort,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "author", defaultValue = "") String author,
            Authentication auth, Model model) {

        var pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(sort, sortBy));

        var user = (DAuthUserDetails) auth.getPrincipal();
        if (!user.isEditor()) {
            author = user.getUser();
        }

        Page<Activity> activities;
        if (author.isBlank()) {
            activities = activityRepository.findAll(pageable);
        } else {
            activities = activityRepository.findAllByAuthor(author, pageable);
        }

        // Populate model
        model.addAttribute("page", page);
        model.addAttribute("activities", activities);
        model.addAttribute("author", author);
        model.addAttribute("receptionMode", darkmode.getCurrent());

        return "activities/list";
    }
}
