package com.werewolf.gameplay;

public interface AlignmentInterface {

    /** The alignment the role is fighting for **/
    public String getAlignmentName();

    /** A short descriptive requirement to win the game **/
    public String getGoal();

    /** Returns an array of roles from a specific alignment **/
    public Roles[] getAllRolesFromThisAlignment();

    /** Retruns a random role from a specific alignment **/
    public Roles getRandomRoleFromThisAlignment();
}
