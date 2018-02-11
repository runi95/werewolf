package com.werewolf.gameplay;

import java.util.Random;

public enum Alignments implements AlignmentInterface {
    Good {
        public static final String alignment = "Good";
        public static final String goal = "Survive and kill all evildoers";

        public final Roles[] alignmentUnique = new Roles[] { /*Roles.King*/ };
        public final Roles[] alignmentNonUnique = new Roles[] { /*Roles.Inquisitor, Roles.Bard, Roles.Priest, Roles.Guard*/ };

        public String getAlignmentName() {
            return "Good"; }

        public String getGoal() {
            return "Survive and kill all evildoers"; }

        public Roles[] getAllRolesFromThisAlignment() {
            Roles[] all = new Roles[alignmentUnique.length + alignmentNonUnique.length];
            for (int i = 0; i < alignmentUnique.length; i++)
                all[i] = alignmentUnique[i];

            for (int i = 0; i < alignmentNonUnique.length; i++)
                all[alignmentUnique.length + i] = alignmentNonUnique[i];

            return all;
        }

        public Roles getRandomRoleFromThisAlignment() {
            Random random = new Random();
            int rng = random.nextInt(alignmentNonUnique.length);
            Roles randomChaoticGood = alignmentNonUnique[rng];

            return randomChaoticGood;
        }
    },

    ChaoticGood {
        public final Roles[] alignmentUnique = new Roles[] {};
        public final Roles[] alignmentNonUnique = new Roles[] { /*Roles.Amnesiac*/ };

        public String getAlignmentName() {
            return "Chaotic Good"; }

        public String getGoal() {
            return "Survive and kill all evildoers"; }

        public Roles[] getAllRolesFromThisAlignment() {
            Roles[] all = new Roles[alignmentUnique.length + alignmentNonUnique.length];
            for (int i = 0; i < alignmentUnique.length; i++)
                all[i] = alignmentUnique[i];

            for (int i = 0; i < alignmentNonUnique.length; i++)
                all[alignmentUnique.length + i] = alignmentNonUnique[i];

            return all;
        }

        public Roles getRandomRoleFromThisAlignment() {
            Random random = new Random();
            int rng = random.nextInt(alignmentNonUnique.length);
            Roles randomChaoticGood = alignmentNonUnique[rng];

            return randomChaoticGood;
        }
    },

    Neutral {
        public final Roles[] alignmentUnique = new Roles[] {};
        public final Roles[] alignmentNonUnique = new Roles[] { /*Roles.Jester*/ };

        public String getAlignmentName() {
            return "Neutral"; }

        public String getGoal() {
            return "Survive until the end of the game";
        }

        public Roles[] getAllRolesFromThisAlignment() {
            Roles[] all = new Roles[alignmentUnique.length + alignmentNonUnique.length];
            for (int i = 0; i < alignmentUnique.length; i++)
                all[i] = alignmentUnique[i];

            for (int i = 0; i < alignmentNonUnique.length; i++)
                all[alignmentUnique.length + i] = alignmentNonUnique[i];

            return all;
        }

        public Roles getRandomRoleFromThisAlignment() {
            Random random = new Random();
            int rng = random.nextInt(alignmentNonUnique.length);
            Roles randomChaoticGood = alignmentNonUnique[rng];

            return randomChaoticGood;
        }
    },

    NeutralEvil {
        public final Roles[] alignmentUnique = new Roles[] {};
        public final Roles[] alignmentNonUnique = new Roles[] {};

        public String getAlignmentName() {
            return "Neutral Evil"; }

        public String getGoal() {
            return "Survive until all members of town are dead"; }

        public Roles[] getAllRolesFromThisAlignment() {
            Roles[] all = new Roles[alignmentUnique.length + alignmentNonUnique.length];
            for (int i = 0; i < alignmentUnique.length; i++)
                all[i] = alignmentUnique[i];

            for (int i = 0; i < alignmentNonUnique.length; i++)
                all[alignmentUnique.length + i] = alignmentNonUnique[i];

            return all;
        }

        public Roles getRandomRoleFromThisAlignment() {
            Random random = new Random();
            int rng = random.nextInt(alignmentNonUnique.length);
            Roles randomChaoticGood = alignmentNonUnique[rng];

            return randomChaoticGood;
        }
    },

    ChaoticEvil {
        public final Roles[] alignmentUnique = new Roles[] {};
        public final Roles[] alignmentNonUnique = new Roles[] {};

        public String getAlignmentName() {
            return "Chaotic Evil"; }

        public String getGoal() {
            return "Survive and until every member of the town is dead"; }

        public Roles[] getAllRolesFromThisAlignment() {
            Roles[] all = new Roles[alignmentUnique.length + alignmentNonUnique.length];
            for (int i = 0; i < alignmentUnique.length; i++)
                all[i] = alignmentUnique[i];

            for (int i = 0; i < alignmentNonUnique.length; i++)
                all[alignmentUnique.length + i] = alignmentNonUnique[i];

            return all;
        }

        public Roles getRandomRoleFromThisAlignment() {
            Random random = new Random();
            int rng = random.nextInt(alignmentNonUnique.length);
            Roles randomChaoticGood = alignmentNonUnique[rng];

            return randomChaoticGood;
        }
    },

    Evil {
        public final Roles[] alignmentUnique = new Roles[] { /*Roles.Marauder*/ };
        public final Roles[] alignmentNonUnique = new Roles[] { /*Roles.Bandit*/ };

        public String getAlignmentName() {
            return "Evil"; }

        public String getGoal() {
            return "Survive and kill every member of the town"; }

        public Roles[] getAllRolesFromThisAlignment() {
            Roles[] all = new Roles[alignmentUnique.length + alignmentNonUnique.length];
            for (int i = 0; i < alignmentUnique.length; i++)
                all[i] = alignmentUnique[i];

            for (int i = 0; i < alignmentNonUnique.length; i++)
                all[alignmentUnique.length + i] = alignmentNonUnique[i];

            return all;
        }

        public Roles getRandomRoleFromThisAlignment() {
            Random random = new Random();
            int rng = random.nextInt(alignmentNonUnique.length);
            Roles randomChaoticGood = alignmentNonUnique[rng];

            return randomChaoticGood;
        }
    };
}
