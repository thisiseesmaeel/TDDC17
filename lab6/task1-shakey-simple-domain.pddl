(define (domain shakey_domain_simple)
  (:requirements  :adl :typing)
  
  (:types
   room
   normal_door
   shakey
   )
   
  (:predicates
  (at  ?s - shakey ?r - room)
  (normal_passage ?nd - normal_door ?r1 ?r2 - room)		;This is true if there is a normal door between room 1 and room 2

  )

  (:action change_room                                
	:parameters (?s - shakey ?r1 - room ?r2 - room ?nd - normal_door)

	:precondition (or(
        and (at ?s ?r1)(normal_passage ?nd ?r1 ?r2)
    )
    (
        and (at ?s ?r1)(normal_passage ?nd ?r2 ?r1)
    )
  )

	:effect (at ?s ?r2)
   )

)
