(define (problem find-object)
	(:domain shakey_domain)
	(:objects 
        sw1 sw2 sw3    - switch
        r1 r2 r3       - room
        b              - box
        nd1 nd2        - normal_door
        wd1            - wide_door
        s              - shakey
        so             - small_object
        g              - lable
        g1 g2          - gripper
    )
	(:init
	   (at s r2) (exist b r1) (s_exist so r1)
       (belongs sw1 r1) (belongs sw2 r2) (belongs sw3 r3)
       (turned_on sw1) (turned_on sw2) (turned_on sw3)
       (normal_passage nd1 r1 r2) (normal_passage nd2 r2 r3) (wide_passage wd1 r2 r3) (labled so g)
       (free_gripper g1) (free_gripper g2)
	)

    (:goal 
        (at s r1)
    )
)