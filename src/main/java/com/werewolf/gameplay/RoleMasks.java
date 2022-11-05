package com.werewolf.gameplay;

public enum RoleMasks implements RoleInterface {
    Drowned {
        public String getName() {
            return "Drowned";
        }

        public String getDescription() {
            return "";
        }

        public String getInquestMessage() {
            return "";
        }

        public Alignments getAlignment() {
            return Alignments.Neutral;
        }

        public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {

        }

    };
}
