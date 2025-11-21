package com.zygoo132.studyspace.controller;

import com.zygoo132.studyspace.entity.ServiceEntity;
import com.zygoo132.studyspace.entity.ServiceInventory;
import com.zygoo132.studyspace.repository.ServiceEntityRepository;
import com.zygoo132.studyspace.repository.ServiceInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceEntityRepository serviceRepo;

    @Autowired
    private ServiceInventoryRepository inventoryRepo;

    // DANH SÁCH + FORM THÊM
    @GetMapping("/list")
    public String list(Model model) {
        List<ServiceEntity> services = serviceRepo.findAll();
        model.addAttribute("services", services);
        model.addAttribute("newService", new ServiceEntity());
        return "service_list";
    }

    // THÊM DỊCH VỤ MỚI
    @PostMapping("/add")
    public String addService(@ModelAttribute("newService") ServiceEntity service,
                             @RequestParam("totalQuantity") Integer totalQuantity) {

        serviceRepo.save(service);

        ServiceInventory inv = new ServiceInventory();
        inv.setService(service);
        inv.setTotalQuantity(totalQuantity);
        inventoryRepo.save(inv);

        return "redirect:/service/list";
    }

    // FORM SỬA
    @GetMapping("/edit/{id}")
    public String editService(@PathVariable Long id, Model model) {

        ServiceEntity service = serviceRepo.findById(id).orElseThrow();
        ServiceInventory inv = inventoryRepo.findByService_ServiceId(id)
                .orElseGet(() -> {
                    ServiceInventory i = new ServiceInventory();
                    i.setService(service);
                    i.setTotalQuantity(0);
                    return i;
                });

        model.addAttribute("service", service);
        model.addAttribute("inventory", inv);
        return "service_edit";
    }

    // CẬP NHẬT
    @PostMapping("/update")
    public String updateService(
            @ModelAttribute("service") ServiceEntity service,
            @RequestParam(value = "inventoryId", required = false) Long inventoryId,
            @RequestParam("totalQuantity") Integer totalQuantity
    ) {

        serviceRepo.save(service);

        ServiceInventory inv;

        if (inventoryId != null) {
            inv = inventoryRepo.findById(inventoryId).orElse(new ServiceInventory());
        } else {
            inv = new ServiceInventory();
            inv.setService(service);
        }

        inv.setTotalQuantity(totalQuantity);

        inventoryRepo.save(inv);

        return "redirect:/service/list";
    }


    // XOÁ
    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {

        inventoryRepo.findByService_ServiceId(id)
                .ifPresent(inventoryRepo::delete);

        serviceRepo.deleteById(id);

        return "redirect:/service/list";
    }
}
