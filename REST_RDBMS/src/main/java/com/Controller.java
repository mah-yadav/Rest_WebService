package com;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/student")
public class Controller {
	
	@RequestMapping(value="/",method = RequestMethod.GET)
	public List<Student> get(){
		List<Student> list = new ArrayList<Student>();
		Student student = new Student();
		student.setAge(29);
		student.setName("Mahesh");
		list.add(student);
		
		return list;
	}

}
