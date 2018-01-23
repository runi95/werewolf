package com.werewolf.gameplay;

public enum Roles implements RoleInterface {
    Amnesiac{
        public String getName() {
            return "Amnesiac"; }

        public String getDescription() {
            return "As an amnesiac you're unable to remember your true role until day 3"; }

        public String getInquestMessage() {
            return "Your target seems to be a lunatic. (Amnesiac, Jester)"; }

        public Alignments getAlignment() {
            return Alignments.ChaoticGood;
        }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.amnesiac(self); }
    },

    Bandit {
        public String getName() {
            return "Bandit"; }

        public String getDescription() {
            return "You're a bandit, a simple follower of the boss, you can head out at night to kill someone. If the boss dies you'll automatically be promoted"; }

        public String getInquestMessage() {
            return "Your target seems familiar with weapons. (Bandit, Guard)"; }

        public Alignments getAlignment() {
            return Alignments.Evil; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.voteEvilKill(self, target); }
    },

    Bard {
        public String getName() {
            return "Bard"; }

        public String getDescription() {
            return "You're a bard, you're an expert at keeping others busy at night by occupying them, negating any and all of their actions that night"; }

        public String getInquestMessage() {
            return "Your target seems to be popular amongst others. (Bard, Priest, Illusionist)"; }

        public Alignments getAlignment() {
            return Alignments.Good; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.block(self, target); }
    },

    Guard {
        public String getName() {
            return "Guard"; }

        public String getDescription() {
            return "You are the town Guard, you're here to serve and protect, you'll be awake all night protecting the target of your choice, be careful though as you could accidentally protect an evildoer!"; }

        public String getInquestMessage() {
            return "Your target seems familiar with weapons. (Bandit, Guard)"; }

        public Alignments getAlignment() {
            return Alignments.Good; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.guard(self, target); }
    },

    Inquisitor {
        public String getName() {
            return "Inquisitor"; }

        public String getDescription() {
            return "You're the inquisitor, your role is to investigate suspects at night and possibly find out who they are"; }

        public String getInquestMessage() {
            return "Your target seems to hold a lot of information. (Inquisitor)"; }

        public Alignments getAlignment() {
            return Alignments.Good; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.inquest(self, target); }
    },

    Jester {
        public String getName() {
            return "Jester"; }

        public String getDescription() {
            return ""; }

        public String getInquestMessage() {
            return "Your target seems to be a lunatic. (Amnesiac, Jester)"; }

        public Alignments getAlignment() {
            return Alignments.Neutral; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            }
    },

    Mermaid {
        public String getName() {
            return "Mermaid"; }

        public String getDescription() {
            return "Drown people at night and try to survive until the end-game. Drowned won't have their job revealed during the day phase."; }

        public String getInquestMessage() {
            return "Your target seems to be a lunatic. (Amnesiac, Jester)"; }

        public Alignments getAlignment() {
            return Alignments.NeutralEvil; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.sirenKill(self, target);
        }
    },

    King {
        public String getName() {
            return "King"; }

        public String getDescription() {
            return "You are the town's king, you can sway any vote in your favour, making you an incredibly important asset late in the game, beware though if you ever vote during the day you'll automatically reveal yourself as the king informing any and every evildoer out there of your power"; }

        public String getInquestMessage() {
            return "Your target seems to be of high importance. (King, Marauder)"; }

        public Alignments getAlignment() {
            return Alignments.Good; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
             }
    },

    Knight {
        public String getName() {
            return "Knight"; }

        public String getDescription() {
            return "As a knight you can decide to get up at night and kill someone, beware that killing a member of the town will get you executed the next day"; }

        public String getInquestMessage() {
            return "Your target seems to be skilled with swords. (Knight)"; }

        public Alignments getAlignment() {
            return Alignments.Good; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.knightKill(self, target); }
    },

    Marauder {
        public String getName() {
            return "Marauder"; }

        public String getDescription() {
            return "You're the boss, the leader of an evil organisation that plans to take over the town, order someone to be killed or head out to kill them yourself if all your minions are dead"; }

        public String getInquestMessage() {
            return "Your target seems to be of high importance. (King, Marauder)"; }

        public Alignments getAlignment() {
            return Alignments.Evil; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.forceEvilKill(self, target); }
    },

    Priest {
        public String getName() {
            return "Priest"; }

        public String getDescription() {
            return "You're the town priest, your role is to make sure everyone makes it through the night, you get to heal someone every night and make sure they can't die"; }

        public String getInquestMessage() {
            return "Your target seems to be popular amongst others. (Bard, Priest, Illusionist)"; }

        public Alignments getAlignment() {
            return Alignments.Good; }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
            game.heal(self, target); }
    };
}
