(define (domain shakey_world)
  (:requirements  :adl
		  :typing
		  )
  
  (:types
   room
   wide_door
   normal_door
   switch
   box
   small_object
   shakey
   gripper
   )
   
  (:predicates
  (at  ?s - shakey ?r - room)
  (belongs  ?sw - switch ?r - room)
  (exist  ?b - box ?r - room)
  (under ?b - box ?sw - switch)
  (s_exist  ?so - small_object r - room)
  (occupied_gripper ?g - gripper)
  (on ?s - shakey ?b - box)
  (turned_on ?sw - switch)
  (wide_passage ?wd - wide_door ?r1 ?r2 - room)			;This is true if there is a wide door between room 1 and room 2 
  (normal_passage ?nd - normal_door ?r1 ?r2 - room)		;This is true if there is a normal door between room 1 and room 2 
  )
  
  (:action push_to_swtich                                
	:parameters (?s - shakey ?b - box ?r - room ?sw - switch)

	:precondition (and (at ?s ?r)
		      	   (exist ?b ?r)
		      	   (belong ?sw ?r))

	:effect (under ?b ?sw)
   )

 (:action move_to_room                                
	:parameters (?s - shakey ?wd - wide_door ?b - box ?r1 ?r2 - room)

	:precondition (and (at ?s ?r1)
		      	   (exist ?b ?r1)
			   (wide_passage ?wd ?r1 ?r2))

	:effect (and (at ?s ?r2) (exist ?b ?r2))
 )

 (:action find                                
	:parameters (?s - shakey ?s - switch ?sm - smal_object ?r - room)

	:precondition (and (at ?s ?r)
		      	   (s_exist ?sm ?r)
			   (belongs ?sw ?r)
			   (turned_on ?sw))

	:effect() ; WHAT IS THE EFFECT OF FIND ???
 )

 (:action climb                                
	:parameters (?s - shakey ?b - box ?r - room)

	:precondition (and (at ?s ?r)
		      	   (exist ?b ?r))

	:effect(and (on ?s ?b))
 )

 )
