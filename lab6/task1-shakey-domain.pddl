(define (domain shakey_domain)
  (:requirements  :adl :typing)
  
  (:types
   room
   wide_door
   normal_door
   switch
   box
   small_object
   shakey
   gripper
   lable
   )
   
  (:predicates
  (at  ?s - shakey ?r - room)
  (belongs  ?sw - switch ?r - room)
  (exist  ?b - box ?r - room)
  (under ?b - box ?sw - switch)
  (s_exist ?so - small_object ?r - room)
  (free_gripper ?g - gripper)
  (on ?s - shakey ?b - box)
  (turned_on ?sw - switch)
  (wide_passage ?wd - wide_door ?r1 ?r2 - room)			;This is true if there is a wide door between room 1 and room 2 
  (normal_passage ?nd - normal_door ?r1 ?r2 - room)		;This is true if there is a normal door between room 1 and room 2
  (labled ?so - small_object ?lb - lable)
  (found ?s - shakey ?sm - small_object)
  )

  (:action push_to_swtich                                
	:parameters (?s - shakey ?b - box ?r - room ?sw - switch)

	:precondition (and (at ?s ?r)
		      	   (exist ?b ?r)
		      	   (belongs ?sw ?r)
               (not(on ?s ?b)))

	:effect (under ?b ?sw)
   )

  (:action change_room                                
	:parameters (?s - shakey ?r1 ?r2 - room ?nd - normal_door ?wd - wide_door)

	:precondition (or
    (and (at ?s ?r1)(or(normal_passage ?nd ?r1 ?r2) (normal_passage ?nd ?r2 ?r1)))
    
    (and (at ?s ?r1) (or(wide_passage ?wd ?r1 ?r2) (wide_passage ?wd ?r2 ?r1)))
    
  )

	:effect (at ?s ?r2)
   )

 (:action move_to_room                                
	:parameters (?s - shakey ?wd - wide_door ?b - box ?r1 ?r2 - room)

	:precondition (and 
          (at ?s ?r1)(exist ?b ?r1)
			    (or(wide_passage ?wd ?r1 ?r2)(or(wide_passage ?wd ?r2 ?r1)))
        )

	:effect (and (at ?s ?r2) (exist ?b ?r2))
 )

  (:action move_small_object_to_room                                
	:parameters (?s - shakey ?wd - wide_door ?nd - normal_door ?so - small_object ?r1 ?r2 - room ?g1 ?g2 - gripper)

	:precondition (or
          (and (at ?s ?r1) (s_exist ?so ?r1) (or(normal_passage ?nd ?r1 ?r2) (normal_passage ?nd ?r2 ?r1)) (found ?s ?so) (or(free_gripper ?g1) (free_gripper ?g2))) 
                  
          (and (at ?s ?r1) (s_exist ?so ?r1) (or(wide_passage ?wd ?r1 ?r2) (wide_passage ?wd ?r2 ?r1)) (found ?s ?so) (or(free_gripper ?g1) (free_gripper ?g2)))
  )

	:effect (and (at ?s ?r2) (s_exist ?so ?r2))
 )

 (:action find                                
	:parameters (?s - shakey ?sw - switch ?so - small_object ?r - room, ?lb - lable)

	:precondition (and (at ?s ?r)
		      	   (s_exist ?so ?r)
              (belongs ?sw ?r)
              (turned_on ?sw)
              (labled ?so ?lb))

	:effect(and(found ?s ?so))
 )


(:action turn_on_switch                                
	:parameters (?s - shakey ?sw - switch ?r - room, ?b - box)

	:precondition (and (at ?s ?r)
              (belongs ?sw ?r)
              (on ?s ?b)
              (under ?b ?sw))

	:effect(and(turned_on ?sw))
 )

 (:action climb
     :parameters (?s - shakey ?b - box ?r - room)
     :precondition (and (at ?s ?r) (exist ?b ?r))
     :effect (and (on ?s ?b))
 )

 )