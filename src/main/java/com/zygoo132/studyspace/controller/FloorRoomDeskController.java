package com.zygoo132.studyspace.controller;

import com.zygoo132.studyspace.entity.Floor;
import com.zygoo132.studyspace.entity.Room;
import com.zygoo132.studyspace.entity.Desk;
import com.zygoo132.studyspace.enums.LoaiBan;
import com.zygoo132.studyspace.enums.LoaiPhong;
import com.zygoo132.studyspace.repository.FloorRepository;
import com.zygoo132.studyspace.repository.RoomRepository;
import com.zygoo132.studyspace.repository.DeskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/floor")
public class FloorRoomDeskController {

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DeskRepository deskRepository;

    @GetMapping("/list")
    public String listFloors(Model model) {
        List<Floor> floors = floorRepository.findAll();
        model.addAttribute("floors", floors);
        model.addAttribute("LoaiPhong", LoaiPhong.values());
        model.addAttribute("LoaiBan", LoaiBan.values());
        return "manage_floor";
    }

    @PostMapping("/delete/{id}")
    public String deleteFloor(@PathVariable Long id) {
        floorRepository.deleteById(id);
        return "redirect:/floor/list";
    }


    @PostMapping("/room/add")
    public String addRoom(
            @RequestParam Long floorId,
            @RequestParam String roomName,
            @RequestParam LoaiPhong roomType
    ) {
        Floor floor = floorRepository.findById(floorId).orElseThrow();
        Room room = new Room();
        room.setRoomName(roomName);
        room.setRoomType(roomType);
        room.setFloor(floor);

        roomRepository.save(room);
        return "redirect:/floor/list";
    }


    @PostMapping("/room/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomRepository.deleteById(id);
        return "redirect:/floor/list";
    }


    @PostMapping("/desk/add")
    public String addDesk(
            @RequestParam Long roomId,
            @RequestParam String deskName,
            @RequestParam Integer capacity,
            @RequestParam LoaiBan deskType
    ) {
        Room room = roomRepository.findById(roomId).orElseThrow();

        Desk d = new Desk();
        d.setDeskName(deskName);
        d.setCapacity(capacity);
        d.setDeskType(deskType);
        d.setRoom(room);

        deskRepository.save(d);
        return "redirect:/floor/list";
    }


    @PostMapping("/desk/delete/{id}")
    public String deleteDesk(@PathVariable Long id) {
        deskRepository.deleteById(id);
        return "redirect:/floor/list";
    }

}
