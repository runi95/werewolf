package com.werewolf.gameplay;

import java.util.HashMap;
import java.util.LinkedList;

public class GameEmulator {
	private HashMap<EmulationCharacter, EmulationCharacter> blockers = new HashMap<>();
	private HashMap<EmulationCharacter, EmulationCharacter> blockersReversed = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> killers = new HashMap<>();
	private HashMap<EmulationCharacter, EmulationCharacter> killersReversed = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> healers = new HashMap<>();
	private HashMap<EmulationCharacter, EmulationCharacter> healersReversed = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> guards = new HashMap<>();
	private HashMap<EmulationCharacter, EmulationCharacter> guardsReversed = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> inquisitors = new HashMap<>();
	private HashMap<EmulationCharacter, EmulationCharacter> inquisitorsReversed = new HashMap<>();

	private LinkedList<EmulationCharacter> amnesiacs = new LinkedList<>();

	private LinkedList<EmulationCharacter> deadPlayers = new LinkedList<>();

	private int day;

	public GameEmulator(int day) {
		this.day = day;
	}

	public void block(EmulationCharacter blocker, EmulationCharacter target) {
		blockers.put(blocker, target);
		blockersReversed.put(target, blocker);
	}

	public void guard(EmulationCharacter guard, EmulationCharacter target) {
		guards.put(guard, target);
		guardsReversed.put(target, guard);
	}

	public void kill(EmulationCharacter killer, EmulationCharacter target) {
		killers.put(killer, target);
		killersReversed.put(target, killer);
	}

	public void heal(EmulationCharacter healer, EmulationCharacter target) {
		healers.put(healer, target);
		healersReversed.put(target, healer);
	}

	public void inquest(EmulationCharacter inquisitor, EmulationCharacter target) {
		inquisitors.put(inquisitor, target);
		inquisitorsReversed.put(target, inquisitor);
	}

	public void amnesiac(EmulationCharacter amnesiac) {
		amnesiacs.add(amnesiac);
	}

	public LinkedList<EmulationCharacter> getDeadPlayers() {
		return deadPlayers;
	}

	public void emulate() {
		emulateBlockers();
		emulateKillersGuardsHealers();
		emulateAmnesiacs();
		emulateInquisitors();
	}

	private void emulateBlockers() {
		for (EmulationCharacter blocker : blockers.keySet()) {
			EmulationCharacter blocked = blockers.get(blocker);

			if (blocked != null) {
				blocked.addMessage("Someone kept you occupied during the night.");
				EmulationCharacter target;

				target = killers.get(blocked);
				if (target != null) {
					killers.remove(blocked);
					killersReversed.remove(target);
					break;
				}

				target = healers.get(blocked);
				if (target != null) {
					healers.remove(blocked);
					healersReversed.remove(target);
				}

				target = inquisitors.get(blocked);
				if (target != null) {
					inquisitors.remove(blocked);
					inquisitorsReversed.remove(target);
				}
			}
		}
	}

	private void emulateKillersGuardsHealers() {
		for (EmulationCharacter killer : killers.keySet()) {
			EmulationCharacter killed = killers.get(killer);

			if (killed != null) {
				System.out.println("A " + killer.getRole().getName() + " has tried to kill a " + killed.getRole().getName());
				EmulationCharacter guarded = guardsReversed.get(killed);
				if (guarded != null) {
					EmulationCharacter guard = guards.get(guarded);
					guarded.addMessage("Someone attacked you during the night, but a guard saved you.");
					killer.addMessage("Someone was guarding your target.");
					guard.addMessage("Someone attacked your target.");
					deadPlayers.add(killer);
					deadPlayers.add(guard);
				} else {
					EmulationCharacter healed = healersReversed.get(killed);
					if (healed != null) {
						EmulationCharacter healer = healersReversed.get(healed);
						healed.addMessage("Someone attacked you during the night, but a priest healed you.");
						healer.addMessage("Your target was attacked, but you saved them.");
					} else {
						killed.addMessage("Someone attacked you.");
						deadPlayers.add(killed);
					}
				}
			}
		}
	}

	private void emulateInquisitors() {
		for (EmulationCharacter inquisitor : inquisitors.keySet()) {
			EmulationCharacter inquest = inquisitors.get(inquisitor);
			if (inquest != null)
				inquisitor.addMessage(inquest.getInquestMessage());
		}
	}

	private void emulateAmnesiacs() {
		for (EmulationCharacter amnesiac : amnesiacs) {
			if (day >= 3) {
				amnesiac.setRole(Good.getRandomGood());
				amnesiac.addMessage("You just remembered that you're a " + amnesiac.getRole().getName() + ".");
			} else {
				amnesiac.addMessage("You struggle to remember who you are.");
			}
		}
	}
}
