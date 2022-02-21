package es.urjc.dad.poshart.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.SessionData;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private SessionData sessionData;
	
	@GetMapping("/{id}")
	public String getMuro(Model model, @PathVariable long id, @RequestParam(defaultValue = "-1") long colId) {
		User u = userRepository.findById(id).orElseThrow();
		model.addAttribute("follows", u.getFollows().size());
		model.addAttribute("followers", u.getFollowers().size());
		model.addAttribute("user", u);
		if(colId!=-1) {
			Collection c = collectionRepository.findById(colId).orElseThrow();
			model.addAttribute("selectedCol", c);
		}
		boolean isMine = id==sessionData.getUser();
		model.addAttribute("isMine", isMine);
		if(!isMine) {
			User other = userRepository.findById(sessionData.getUser()).orElseThrow();
			boolean followed = u.getFollows().contains(other);
			model.addAttribute("followed", followed);
		}
		return "muro";
	}
}
