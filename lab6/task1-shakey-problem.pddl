(define (problem find-object)
	(:domain shakey_domain)
	(:objects 
        sw1 sw2 sw3    - switch
        r1 r2 r3       - room
        b              - box
        nd            - normal_door
        wd1 wd2            - wide_door
        s              - shakey
        so             - small_object
        g              - lable
        g1 g2          - gripper
    )
	(:init
	   (at s r2) (exist b r1) (s_exist so r1)
       (belongs sw1 r1) (belongs sw2 r2) (belongs sw3 r3)
       (turned_on sw2) (turned_on sw3)
       (wide_passage wd1 r1 r2) (normal_passage nd r2 r3) (wide_passage wd2 r2 r3) (labled so g)
       (free_gripper g1) (free_gripper g2)
	)

    (:goal 
        (found s so)
    )
)