(define (problem change-room)
	(:domain shakey_domain_simple)
	(:objects 
        r1 r2       - room
        nd          - normal_door
        s           - shakey
    )
	(:init
	   (at s r1) 
       (normal_passage nd r1 r2)
	)

    (:goal 
        (at s r2)
    )
)