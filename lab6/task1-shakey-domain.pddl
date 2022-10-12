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
  (at  ?s - shakey ?r - room)           ; This predicate is for locating "Shakey"
  (belongs  ?sw - switch ?r - room)  ; This predicate is for locating switches
  (exist  ?b - box ?r - room)     ; This predicate is for locating boxes
  (under ?b - box ?sw - switch)        ; This predicate is for showing if a box is under a switch
  (s_exist ?so - small_object ?r - room)  ; This predicate is for locating small objects 
  (free_gripper ?g - gripper)       ; This predicate is for indicating if Shakey has free gripper
  (on ?s - shakey ?b - box)             ; This predicate is for showing if Shakey is on a box
  (turned_on ?sw - switch)              ; This predicate is for showing status of a switch (on or off)
  (wide_passage ?wd - wide_door ?r1 ?r2 - room)			;This is true if there is a wide door between room 1 and room 2 
  (normal_passage ?nd - normal_door ?r1 ?r2 - room)		;This is true if there is a normal door between room 1 and room 2
  (labled ?so - small_object ?lb - lable)       ; This predicate is for assigning a small object with a lable
  (found ?s - shakey ?sm - small_object ?lb - lable)            ; This predicate is for indicating if Shakey has found an small object
  )


  ; This action is for pushing a box to a switch
  ; There must exist a box in the same room in order to do that
  (:action push_box_to_swtich                                
	:parameters (?s - shakey ?b - box ?r - room ?sw - switch)

	:precondition (and (at ?s ?r)
		      	   (exist ?b ?r)
		      	   (belongs ?sw ?r)
               (not(on ?s ?b)))

	:effect (under ?b ?sw)
   )



  ; This action is for Shakey to go to another room
  ; Obviously there must be a passage (either wide or normal) between the rooms
  (:action change_room                                
	:parameters (?s - shakey ?r1 ?r2 - room ?nd - normal_door ?wd - wide_door)

	:precondition (or
    (and (at ?s ?r1)(or(normal_passage ?nd ?r1 ?r2) (normal_passage ?nd ?r2 ?r1)))
    
    (and (at ?s ?r1) (or(wide_passage ?wd ?r1 ?r2) (wide_passage ?wd ?r2 ?r1)))
    
  )

	:effect (at ?s ?r2)
   )



  ; This action is for Shakey to move a box from one room to another
  ; There must be a wide door between the rooms
 (:action move_box_to_room                                
	:parameters (?s - shakey ?wd - wide_door ?b - box ?r1 ?r2 - room)

	:precondition (and 
          (at ?s ?r1)(exist ?b ?r1)
			    (or(wide_passage ?wd ?r1 ?r2)(or(wide_passage ?wd ?r2 ?r1)))
        )

	:effect (and (at ?s ?r2) (exist ?b ?r2))
 )


  ; This action is for carrying (or moving) an small object from one room to another
  ; There must be a passage (wide or normal) between rooms and Shakey must has at least one free hand (gripper) and Shakey must have already found that small object
  (:action move_small_object_to_room                                
	:parameters (?s - shakey ?wd - wide_door ?nd - normal_door ?so - small_object ?lb - lable ?r1 ?r2 - room ?g1 ?g2 - gripper)

	:precondition (or
          (and (at ?s ?r1) (s_exist ?so ?r1) (or(normal_passage ?nd ?r1 ?r2) (normal_passage ?nd ?r2 ?r1)) (found ?s ?so ?lb) (or(free_gripper ?g1) (free_gripper ?g2))) 
                  
          (and (at ?s ?r1) (s_exist ?so ?r1) (or(wide_passage ?wd ?r1 ?r2) (wide_passage ?wd ?r2 ?r1)) (found ?s ?so ?lb) (or(free_gripper ?g1) (free_gripper ?g2)))
  )

	:effect (and (at ?s ?r2) (s_exist ?so ?r2))
 )


; This action is for Shakey to find an small object in a room with a lable on it
; The light must be turned on and Shakey must be in the same room in order to be able to find the small object 
 (:action find_object                                
	:parameters (?s - shakey ?sw - switch ?so - small_object ?r - room, ?lb - lable)

	:precondition (and (at ?s ?r)
		      	   (s_exist ?so ?r)
              (belongs ?sw ?r)
              (turned_on ?sw)
              (labled ?so ?lb))

	:effect(and(found ?s ?so ?lb))
 )



; This action is for Shakey to turn a switch on
; There must be a switch in the same room where Shakey is there must be and a box under the switch and Shakey must be on that box and also light must be off
(:action turn_on_light                                
	:parameters (?s - shakey ?sw - switch ?r - room, ?b - box)

	:precondition (and (at ?s ?r)
              (belongs ?sw ?r)
              (on ?s ?b)
              (under ?b ?sw)
              (not(turned_on ?sw)))

	:effect(and(turned_on ?sw))
 )

; This action is for Shakey to turn the light off
; There must be a switch in the same room where Shakey is there must be and a box under the switch and Shakey must be on that box and also light must be on
(:action turn_off_light                                
	:parameters (?s - shakey ?sw - switch ?r - room, ?b - box)

	:precondition (and (at ?s ?r)
              (belongs ?sw ?r)
              (on ?s ?b)
              (under ?b ?sw)
              (turned_on ?sw))

	:effect(not(turned_on ?sw))
 )


; This action is for Shakey to be able to climb the box
; The box must exist in the same room where Shakey is
 (:action climb_the_box
     :parameters (?s - shakey ?b - box ?r - room)
     :precondition (and (at ?s ?r) (exist ?b ?r))
     :effect (and (on ?s ?b))
 )

 )