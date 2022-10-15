; #####################################################
; Problem
; #####################################################
; In this problem the goal is to remove all boxes from 
; right room.
; There are five boxes in this problem.

(define (problem find-object2)
	(:domain shakey_domain)
	(:objects 
        left_room middle_room right_room            - room
        b1 b2 b3 b4 b5                              - box
        nd                                          - normal_door
        wd1 wd2                                     - wide_door
        s                                           - shakey
        so  - small_object
        sw - switch
        g1 - gripper
        l1 - lable
    )
	(:init
	   (at s middle_room) (exist b1 right_room) (exist b2 right_room) 
       (exist b3 right_room) (exist b4 middle_room) (exist b5 left_room)
       (wide_passage wd1 left_room middle_room) (normal_passage nd middle_room right_room)
       (wide_passage wd2 middle_room right_room) 
	)
    ; Move all boxes out from left room
    (:goal
        (forall (?b - box) (not(exist ?b right_room)))
    )
)