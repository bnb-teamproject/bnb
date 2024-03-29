
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team.bnb.controllers;

import com.team.bnb.model.BnbUserDetails;
import com.team.bnb.model.Reservations;
import com.team.bnb.model.Storages;
import com.team.bnb.model.Users;
import com.team.bnb.repositories.StoragesRepository;
import com.team.bnb.services.BnbUsersDetailService;
import com.team.bnb.services.ClientService;
import com.team.bnb.services.ReservationsService;
import com.team.bnb.services.StoragesService;
import com.team.bnb.utils.DateParsing;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import static org.apache.tomcat.jni.User.username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Haris
 */
@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    StoragesService storagesService;

    @Autowired
    BnbUsersDetailService bnbUsersDetailService;

    @Autowired
    ReservationsService reservationsService;

    @Autowired
    ClientService clientService;

    @Autowired
    DateParsing dateParsing;

    @RequestMapping(value = "/{cityId}", method = RequestMethod.GET)
    public String chooseCity(ModelMap mm, @PathVariable(name = "cityId") int city_id) {
        mm.addAttribute("storages", storagesService.viewStoragesByCity(city_id));
        Reservations reservation = new Reservations();
        mm.addAttribute("reservation", reservation);
        return "reservation";
    }

    @RequestMapping("/loadClient")
    public String loadClient() {

        return "client";
    }

    @RequestMapping(value = "/prepareReservation", method = RequestMethod.POST)
    public String infoGathering(ModelMap model, WebRequest request, @ModelAttribute("reservation") Reservations reservation, HttpServletRequest session) {
        LocalTime mytime = LocalTime.parse(request.getParameter("timepicker"));
        LocalDate localdate = LocalDate.parse(request.getParameter("date"));
        LocalDateTime ldt = localdate.atTime(mytime);
        int hours1 = Integer.parseInt(request.getParameter("hours"));
        int amount = Integer.parseInt(request.getParameter("amount"));
        LocalDateTime end = ldt.plusHours(hours1);
        Date start = dateParsing.convertToDateViaInstant(ldt);
        Date endtime = dateParsing.convertToDateViaInstant(end);
        reservation.setStart(start);
        reservation.setEnd(endtime);
        reservation.setCost(clientService.calculateCost(hours1, amount));
        reservation.setAmount(amount);
        model.addAttribute("reservation", reservation);
        session.getSession().setAttribute("finalize", reservation);
        model.addAttribute("storage", storagesService.viewStorageByid(reservation.getStorageId()));
        return "doReservation";
    }

    @RequestMapping(value = "/doReserve", method = RequestMethod.POST)
    public String reserveSlot(ModelMap model, @ModelAttribute("reservation") Reservations reservation, HttpServletRequest request) {
        reservationsService.insertReservation((Reservations) request.getSession().getAttribute("finalize"));
        return "client";
    }

    @RequestMapping(value = "viewMyReservations", method = RequestMethod.GET)
    public String viewMyStorages(ModelMap mm, HttpServletRequest session) {
        List<Reservations> reservations = reservationsService.viewReservationsByOwner((int) session.getSession().getAttribute("id"));
        mm.addAttribute("myreservations", reservations);
        List<Storages> storages = storagesService.viewStoragesByOwner((int) session.getSession().getAttribute("id"));
        mm.addAttribute("storages", storages);
        return "viewMyReservations";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(ModelMap mm, @PathVariable("id") int id) {
        reservationsService.deleteById(id);
        return "redirect:/viewMyReservations";
    }
}
