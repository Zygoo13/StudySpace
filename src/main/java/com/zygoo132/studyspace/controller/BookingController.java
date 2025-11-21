package com.zygoo132.studyspace.controller;

import com.zygoo132.studyspace.dto.BookingConfirmDTO;
import com.zygoo132.studyspace.dto.BookingCustomerDTO;
import com.zygoo132.studyspace.dto.BookingRequest;
import com.zygoo132.studyspace.entity.*;
import com.zygoo132.studyspace.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/new")
    public String bookingStep1(Model model) {
        List<Combo> combos = comboRepository.findAll();
        model.addAttribute("combos", combos);
        model.addAttribute("request", new BookingRequest());
        return "booking_step1";
    }


    @PostMapping("/available")
    public String showAvailableRooms(@ModelAttribute("request") BookingRequest request,
                                     Model model) {

        // Lấy combo
        Combo combo = comboRepository.findById(request.getComboId())
                .orElseThrow(() -> new RuntimeException("Combo not found"));

        // Convert thời gian
        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getStartTime());

        LocalDateTime start = date.atTime(time);
        LocalDateTime end   = start.plusHours(combo.getHours());

        // Lấy tất cả tầng
        List<Floor> floors = floorRepository.findAll();

        // Build map Floor -> Room -> List<Desk available>
        Map<Long, Map<Long, List<Desk>>> availableMap = new HashMap<>();

        for (Floor floor : floors) {

            List<Room> rooms = roomRepository.findByFloor_FloorId(floor.getFloorId());
            Map<Long, List<Desk>> roomDeskMap = new HashMap<>();

            for (Room room : rooms) {
                List<Desk> availableDesks = deskRepository.findAvailableDesksInRoom(
                        room.getRoomId(), start, end);

                if (!availableDesks.isEmpty()) {
                    roomDeskMap.put(room.getRoomId(), availableDesks);
                }
            }

            if (!roomDeskMap.isEmpty()) {
                availableMap.put(floor.getFloorId(), roomDeskMap);
            }
        }

        // Đẩy sang UI Step 2
        model.addAttribute("request", request);
        model.addAttribute("combo", combo);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("floors", floors);
        model.addAttribute("availableMap", availableMap);
        Map<Long, Room> roomMap = new HashMap<>();
        for (Room room : roomRepository.findAll()) {
            roomMap.put(room.getRoomId(), room);
        }
        model.addAttribute("roomMap", roomMap);


        return "booking_step2";
    }

    @PostMapping("/customer")
    public String chooseCustomer(
            @RequestParam("date") String date,
            @RequestParam("startTime") String startTime,
            @RequestParam("comboId") Long comboId,
            @RequestParam("peopleCount") Integer peopleCount,
            @RequestParam("deskIds") List<Long> deskIds,
            Model model
    ) {
        BookingCustomerDTO dto = new BookingCustomerDTO();
        dto.setDate(date);
        dto.setStartTime(startTime);
        dto.setComboId(comboId);
        dto.setPeopleCount(peopleCount);
        dto.setDeskIds(deskIds);

        model.addAttribute("customerDTO", dto);
        model.addAttribute("customerFound", null);
        model.addAttribute("searched", false); // CHỈ hiển thị form create sau khi search

        return "booking_step3";
    }


    @PostMapping("/customer/search")
    public String searchCustomer(
            @ModelAttribute("customerDTO") BookingCustomerDTO dto,
            Model model
    ) {
        var customer = customerRepository.findByPhone(dto.getPhone());

        model.addAttribute("customerDTO", dto);
        model.addAttribute("customerFound", customer.orElse(null));
        model.addAttribute("searched", true); // đã search → cho phép hiển thị tạo khách

        return "booking_step3";
    }


    @PostMapping("/customer/create")
    public String createCustomer(
            @ModelAttribute("customerDTO") BookingCustomerDTO dto,
            Model model
    ) {
        Customer c = new Customer();
        c.setPhone(dto.getPhone());
        c.setName(dto.getName());
        customerRepository.save(c);

        model.addAttribute("customerDTO", dto);
        model.addAttribute("customerFound", c);
        model.addAttribute("searched", true);

        return "booking_step3";
    }


    @PostMapping("/customer/select")
    public String selectCustomer(
            @ModelAttribute("customerDTO") BookingCustomerDTO dto,
            @RequestParam("customerId") Long customerId
    ) {
        String deskParam = String.join(",",
                dto.getDeskIds().stream().map(String::valueOf).toList());

        return "redirect:/booking/confirm?"
                + "customerId=" + customerId
                + "&date=" + dto.getDate()
                + "&startTime=" + dto.getStartTime()
                + "&comboId=" + dto.getComboId()
                + "&peopleCount=" + dto.getPeopleCount()
                + "&deskIds=" + deskParam;
    }




    @GetMapping("/confirm")
    public String showConfirmPage(
            @RequestParam Long customerId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam Long comboId,
            @RequestParam Integer peopleCount,
            @RequestParam List<Long> deskIds,
            Model model
    ) {
        BookingConfirmDTO dto = new BookingConfirmDTO();
        dto.setCustomerId(customerId);
        dto.setDate(date);
        dto.setStartTime(startTime);
        dto.setComboId(comboId);
        dto.setPeopleCount(peopleCount);
        dto.setDeskIds(deskIds);

        // Load dữ liệu để hiển thị
        var customer = customerRepository.findById(customerId).orElseThrow();
        var combo = comboRepository.findById(comboId).orElseThrow();

        var desks = deskRepository.findAllById(deskIds);
        var room = desks.get(0).getRoom(); // Tất cả bàn đều cùng 1 phòng

        // Tính thời gian
        LocalDateTime start = LocalDate.parse(date).atTime(LocalTime.parse(startTime));
        LocalDateTime end = start.plusHours(combo.getHours());

        model.addAttribute("dto", dto);
        model.addAttribute("customer", customer);
        model.addAttribute("combo", combo);
        model.addAttribute("desks", desks);
        model.addAttribute("room", room);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "booking_step4";
    }

    @PostMapping("/confirm")
    public String confirmBooking(
            @ModelAttribute("dto") BookingConfirmDTO dto,
            Model model
    ) {
        // Lấy dữ liệu để lưu
        var customer = customerRepository.findById(dto.getCustomerId()).orElseThrow();
        var combo = comboRepository.findById(dto.getComboId()).orElseThrow();
        var desks = deskRepository.findAllById(dto.getDeskIds());

        // Tính thời gian
        LocalDateTime start = LocalDate.parse(dto.getDate())
                .atTime(LocalTime.parse(dto.getStartTime()));
        LocalDateTime end = start.plusHours(combo.getHours());

        // Tạo Reservation
        Reservation r = new Reservation();
        r.setCustomer(customer);
        r.setCombo(combo);
        r.setStartTime(start);
        r.setEndTime(end);
        r.setPeopleCount(dto.getPeopleCount());
        r.setNote(dto.getNote());
        r.setRoom(desks.get(0).getRoom());
        r.setDesks(desks);

        reservationRepository.save(r);

        model.addAttribute("reservationId", r.getReservationId());
        return "booking_success";
    }



}
