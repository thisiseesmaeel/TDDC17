; #####################################################
; Problem
; #####################################################
; This problem is a simulation of the figure of the lab (alternative 2: Shakey's World) with a more challenges.
; Shakey is in the middle room and there is a box in the left room
; We assume that there is an small object in the right room labled "ball"
; All rooms are dark
; We added another small object in the middle room to make it more challenging. It is labled "bag"
; The goal for Shakey here is to find the "ball" in the right room

; #####################################################
; Procuder
; #####################################################
; We excpect the planner to find this plan:
; 1) Shakey needs to change room to left room in order to reach the box
; 2) and 3) Shakey needs to move the box back to middle room and then to the right room
; 4) Shakey needs to move that box to the switch in the right room
; 5) Shakey needs to climb that box in order to reach the switch
; 6) Shakey needs to turn the light on in order to see 
; 7) Now Shakey can find the "ball" in the right room

(define (problem find-object)
	(:domain shakey_domain)
	(:objects 
        sw1 sw2 sw3                                 - switch
        left_room middle_room right_room            - room
        b                                           - box
        nd                                          - normal_door
        wd1 wd2                                     - wide_door
        s                                           - shakey
        so1 so2                                        - small_object
        ball bag                                       - lable
        g1 g2                                       - gripper
    )
	(:init
	   (at s middle_room) (exist b left_room) (s_exist so1 right_room) (s_exist so2 middle_room)
       (belongs sw1 left_room) (belongs sw2 middle_room) (belongs sw3 right_room)
       (wide_passage wd1 left_room middle_room) (normal_passage nd middle_room right_room) (wide_passage wd2 middle_room right_room) 
       (labled so1 ball) (labled so2 bag)
       (free_gripper g1) (free_gripper g2)
	)

    (:goal 
        (found s so1 ball)
    )
)