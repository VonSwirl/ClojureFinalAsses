(require '[cgsx.tools.matcher :refer :all])
(require '[clojure.set :refer :all])
(require '[cgsx.tools.opertator_search :refer :all])

(def world-state-1                                          ;World State = agent and box both on floor
  '#{(Agent agent)
     (isa obj box)
     (manipulable box)
     (isa location platform1)
     (isa location platform2)
     (isa location floor)
     (climbable platform1)
     (climbable platform2)
     (holds nil agent)
     (next-to floor agent)
     (at floor agent)
     (at floor platform1)
     (at floor platform2)
     (on floor box)
     (next-to floor box)
     })

(def world-state-2                                          ;World State = agent on platform2 box on platform1
  '#{(Agent agent)
     (isa obj box)
     (manipulable box)
     (isa location platform1)
     (isa location platform2)
     (isa location floor)
     (climbable platform1)
     (climbed platform2)
     (holds nil agent)
     (on-top platform2 agent)
     (at platform2 agent)
     (at floor platform1)
     (at floor platform2)
     (on platform1 box)
     })

(def world-state-3                                          ;World State3 = agent on floor box on platform1
  '#{(Agent agent)
     (isa obj box)
     (manipulable box)
     (isa location platform1)
     (isa location platform2)
     (isa location floor)
     (climbable platform1)
     (climbable platform2)
     (holds nil agent)
     (at floor agent)
     (next-to floor agent)
     (at floor platform1)
     (at floor platform2)
     (on platform1 box)
     })

(def world-state-4                                          ;World State4 = agent on platform box on floor
  '#{(Agent agent)
     (isa obj box)
     (manipulable box)
     (isa location platform1)
     (isa location platform2)
     (isa location floor)
     (climbed platform1)
     (climbable platform2)
     (on-top platform1 agent)
     (holds nil agent)
     (at platform1 agent)
     (at floor platform1)
     (at floor platform2)
     (on floor box)
     (next-to platform2 box)
     })

;(ops-search world-state-1 '((holds box agent)) ops)            Get agent to hold box...
;(ops-search world-state-1 '((next-to platform1 agent)) ops)    Agent next-to p1...
;(ops-search world-state-1 '((next-to platform2 agent)) ops)    Agent next-to p2...
;(ops-search world-state-1 '((on-top platform1 agent)) ops)     Agent on p1...
;(ops-search world-state-1 '((on-top platform2 agent)) ops)     Agent on p2...
;(ops-search world-state-1 '((on platform1 box)) ops)           Box on p1...
;(ops-search world-state-1 '((on platform2 box)) ops)           Box on p2...
;(ops-search world-state-1 '((on floor box)) ops)               Box on floor...
;(ops-search world-state-1 '((at floor agent)) ops)             Agent on floor...

;(ops-search world-state-2 '((holds box agent)) ops)            Get agent to hold box...
;(ops-search world-state-2 '((next-to platform1 agent)) ops)    Agent next-to p1...
;(ops-search world-state-2 '((next-to platform2 agent)) ops)    Agent next-to p2...
;(ops-search world-state-2 '((on-top platform1 agent)) ops)     Agent on p1...
;(ops-search world-state-2 '((on-top platform2 agent)) ops)     Agent on p2...
;(ops-search world-state-2 '((on platform1 box)) ops)           Box on p1...
;(ops-search world-state-2 '((on platform2 box)) ops)           Box on p2...
;(ops-search world-state-2 '((on floor box)) ops)               Box on floor...
;(ops-search world-state-2 '((at floor agent)) ops)             Agent on floor...

;(ops-search world-state-3 '((holds box agent)) ops)            Get agent to hold box...
;(ops-search world-state-3 '((next-to platform1 agent)) ops)    Agent next-to p1...
;(ops-search world-state-3 '((next-to platform2 agent)) ops)    Agent next-to p2...
;(ops-search world-state-3 '((on-top platform1 agent)) ops)     Agent on p1...
;(ops-search world-state-3 '((on-top platform2 agent)) ops)     Agent on p2...
;(ops-search world-state-3 '((on platform1 box)) ops)           Box on p1...
;(ops-search world-state-3 '((on platform2 box)) ops)           Box on p2...
;(ops-search world-state-3 '((on floor box)) ops)               Box on floor...
;(ops-search world-state-3 '((at floor agent)) ops)             Agent on floor...

;(ops-search world-state-4 '((holds box agent)) ops)            Get agent to hold box...
;(ops-search world-state-4 '((next-to platform1 agent)) ops)    Agent next-to p1...
;(ops-search world-state-4 '((next-to platform2 agent)) ops)    Agent next-to p2...
;(ops-search world-state-4 '((on-top platform1 agent)) ops)     Agent on p1...
;(ops-search world-state-4 '((on-top platform2 agent)) ops)     Agent on p2...
;(ops-search world-state-4 '((on platform1 box)) ops)           Box on p1...
;(ops-search world-state-4 '((on platform2 box)) ops)           Box on p2...
;(ops-search world-state-4 '((on floor box)) ops)               Box on floor...
;(ops-search world-state-4 '((at floor agent)) ops)             Agent on floor...

(def ops
  '{move
    {:pre ((at floor agent)
            (isa location ?location2)
            (at floor ?location2)
            (next-to ?location1 agent)
            )
     :del ((next-to ?location1 agent))
     :add ((next-to ?location2 agent))
     :txt (Agent moved across floor to ?location2)
     }

    move-to-box
    {:pre ((at floor agent)
            (isa obj ?obj2)
            (on floor ?obj2)
            (next-to ?location1 agent)
            )
     :del ((next-to ?location1 agent))
     :add ((next-to ?obj2 agent))
     :txt (Agent moved towards ?obj2)
     }

    climb-on
    {:pre ((next-to ?location agent)
            (climbable ?location)
            (at floor agent))                               ;First needs to move then be next-to
     :del ((next-to ?location agent)
            (climbable ?location)
            (at floor agent))
     :add ((on-top ?location agent)
            (climbed ?location))
     :txt (Agent climbed ?location)
     }

    climb-off
    {:pre ((on-top ?location agent)
            (climbed ?location))
     :del ((on-top ?location agent)
            (climbed ?location)
            (at ?location agent))
     :add ((at floor agent)
            (next-to ?location agent)
            (climbable ?location))
     :txt (Agent climbed-off ?location onto floor)
     }

    pickup
    {:pre ((holds nil agent)
            (at ?location1 agent)
            (next-to ?obj agent)
            (on ?location1 ?obj)
            (manipulable ?obj))
     :del ((on ?location1 ?obj)
            (holds nil agent))
     :add ((holds ?obj agent))
     :txt (Agent picked-up ?obj from ?location1)
     }

    pick-off                                                ;Drops onto platform only
    {:pre ((holds nil agent)
            (on-top ?location agent)
            (on ?location ?obj)
            (manipulable ?obj))
     :del ((holds nil agent)
            (on ?location ?obj))
     :add ((holds ?obj agent))
     :txt (Agent picked-off ?obj from ?location)
     }

    drop                                                    ;Drops onto floor only
    {:pre ((at floor agent)
            (holds ?obj agent)
            (:not (holds nil agent)))
     :del ((holds ?obj agent))
     :add ((holds nil agent)
            (on floor ?obj))
     :txt (Agent dropped ?obj onto floor)
     }

    drop-on
    {:pre ((on-top ?location agent)                         ;Set its location to be the current location
            (holds ?obj agent)
            (:not (holds nil agent)))
     :del ((holds ?obj agent))
     :add ((holds nil agent)
            (on ?location ?obj))
     :txt (Agent dropped ?obj onto ?location)
     }})