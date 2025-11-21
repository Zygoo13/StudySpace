package com.zygoo132.studyspace.controller;

import com.zygoo132.studyspace.entity.Desk;
import com.zygoo132.studyspace.entity.Reservation;
import com.zygoo132.studyspace.repository.DeskRepository;
import com.zygoo132.studyspace.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class DeskStatusController {

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/desks/status")
    public String deskStatus(Model model) {

        List<Desk> desks = deskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        Map<Long, Reservation> deskStatus = new HashMap<>();

        for (Desk d : desks) {
            List<Reservation> actives =
                    reservationRepository.findActiveReservation(d.getDeskId(), now);

            if (!actives.isEmpty()) {
                deskStatus.put(d.getDeskId(), actives.get(0));
            } else {
                deskStatus.put(d.getDeskId(), null);
            }
        }

        model.addAttribute("desks", desks);
        model.addAttribute("deskStatus", deskStatus);
        model.addAttribute("now", now);

        return "desk_status";
    }
}
