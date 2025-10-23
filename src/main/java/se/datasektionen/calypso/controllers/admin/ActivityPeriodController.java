package se.datasektionen.calypso.controllers.admin;

import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintViolationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import se.datasektionen.calypso.config.PeriodFormatter;
import se.datasektionen.calypso.exceptions.FailedValidationException;
import se.datasektionen.calypso.exceptions.ResourceNotFoundException;
import se.datasektionen.calypso.models.entities.ActivityPeriod;
import se.datasektionen.calypso.models.repositories.ActivityPeriodRepository;
import se.datasektionen.calypso.models.repositories.ActivityRepository;

@Controller
@PreAuthorize("hasAuthority('post')")
@RequestMapping("/admin/activities/periods")
@RequiredArgsConstructor
public class ActivityPeriodController {

    private final ActivityRepository activityRepository;
    private final ActivityPeriodRepository periodRepository;

    private String showEditForm(ActivityPeriod period, Model model) {
        model.addAttribute("period", period);

        return "activities/edit-period";
    }

    @GetMapping("/new/{activityId}")
    public String newForm(@PathVariable long activityId, Authentication auth, Model model) {
        var activity = activityRepository.findById(activityId).orElseThrow(ResourceNotFoundException::new);

        var period = new ActivityPeriod();
        period.setActivity(activity);

        return this.showEditForm(period, model);
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable long id, Model model) {
        var period = periodRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        return this.showEditForm(period, model);
    }

    @PostMapping("/edit")
    public String doEdit(@RequestParam long activityId, ActivityPeriod period, BindingResult bindingResult) {
        // check for form errors
        if (bindingResult.hasErrors()) {
            return "activities/edit-period";
        }

        var activity = activityRepository.findById(activityId).orElseThrow(ResourceNotFoundException::new);
        period.setActivity(activity);

        period.setStartTime(period.getStartTime().truncatedTo(ChronoUnit.MINUTES));
        period.setEndTime(period.getEndTime().truncatedTo(ChronoUnit.MINUTES));

        // design choice: not checking for overlaps; users are free to do what they
        // feel is best - there may be valid use cases that require overlapping
        // activity periods (with different recurrences)

        try {
            period = periodRepository.save(period);
        } catch (ConstraintViolationException | TransactionSystemException e) {
            throw new FailedValidationException(e);
        }

        return "redirect:/admin/activities/periods/edit/" + period.getId() + "?saved=true";
    }

    @PreAuthorize("hasAuthority('manage-all')")
    @PostMapping("/delete/{id}")
    public String doDelete(@PathVariable long id) {
        // need to fetch first to extract activity ID for redirect
        var period = periodRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        var activityId = period.getActivity().getId();
        periodRepository.deleteById(id);
        return "redirect:/admin/activities/periods/list/" + activityId + "?deleted=" + id;
    }

    @RequestMapping("/list/{activityId}")
    public String list(@PathVariable long activityId, Authentication auth, Model model) {
        var activity = activityRepository.findById(activityId).orElseThrow(ResourceNotFoundException::new);
        var periods = periodRepository.findAllByActivityIdOrderByIdDesc(activityId);

        model.addAttribute("periodFormatter", PeriodFormatter.class);
        model.addAttribute("activity", activity);
        model.addAttribute("periods", periods);

        return "activities/list-periods";
    }
}
