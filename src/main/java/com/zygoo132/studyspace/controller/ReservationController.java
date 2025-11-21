package com.zygoo132.studyspace.controller;

import com.zygoo132.studyspace.entity.Desk;
import com.zygoo132.studyspace.entity.Reservation;
import com.zygoo132.studyspace.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DeskRepository deskRepository;

    /**
     * Lịch đặt theo ngày
     * GET /reservation/daily?date=2025-11-22
     * Nếu không truyền date -> mặc định hôm nay
     */
    @GetMapping("/daily")
    public String daily(
            @RequestParam(required = false) String date,
            Model model
    ) {
        LocalDate selectedDate = (date == null || date.isBlank())
                ? LocalDate.now()
                : LocalDate.parse(date);

        LocalDateTime startOfDay = selectedDate.atStartOfDay();
        LocalDateTime endOfDay = selectedDate.atTime(LocalTime.MAX);

        List<Reservation> reservations =
                reservationRepository.findByDay(startOfDay, endOfDay);

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("reservations", reservations);

        return "reservation_daily";
    }

    /**
     * Xóa 1 reservation rồi quay lại trang daily đúng ngày
     */
    @GetMapping("/delete/{id}")
    public String deleteReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String date
    ) {
        reservationRepository.deleteById(id);

        // giữ nguyên ngày đang xem, nếu không có thì quay về hôm nay
        if (date != null && !date.isBlank()) {
            return "redirect:/reservation/daily?date=" + date;
        }
        return "redirect:/reservation/daily";
    }

    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {

        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        model.addAttribute("reservation", r);

        // Combo list
        model.addAttribute("combos", comboRepository.findAll());

        // Rooms
        model.addAttribute("rooms", roomRepository.findAll());

        // Desks available theo room
        model.addAttribute("desks", deskRepository.findByRoom_RoomId(r.getRoom().getRoomId()));

        return "reservation_edit";
    }

    @PostMapping("/update")
    public String updateReservation(
            @RequestParam Long reservationId,
            @RequestParam Long roomId,
            @RequestParam Long comboId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam Integer peopleCount,
            @RequestParam(required = false) String note,
            @RequestParam(required = false, name = "deskIds") List<Long> deskIds
    ) {

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        r.setRoom(roomRepository.findById(roomId).orElse(null));
        r.setCombo(comboRepository.findById(comboId).orElse(null));
        r.setStartTime(LocalDateTime.parse(startTime));
        r.setEndTime(LocalDateTime.parse(endTime));
        r.setPeopleCount(peopleCount);
        r.setNote(note);

        // Update desks
        if (deskIds != null) {
            List<Desk> deskList = deskRepository.findAllById(deskIds);
            r.setDesks(deskList);
        } else {
            r.setDesks(List.of());
        }

        reservationRepository.save(r);

        return "redirect:/reservation/daily";
    }




}
