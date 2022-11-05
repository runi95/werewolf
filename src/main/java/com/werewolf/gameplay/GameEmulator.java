package com.werewolf.gameplay;

import com.werewolf.Messages.LobbyMessage;

import java.util.HashMap;
import java.util.LinkedList;

public class GameEmulator {
	private HashMap<EmulationCharacter, EmulationCharacter> blockers = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> killers = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> knights = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> sirens = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> healers = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> guards = new HashMap<>();

	private HashMap<EmulationCharacter, EmulationCharacter> inquisitors = new HashMap<>();

	private EmulationCharacter marauder = null;
	private EmulationCharacter marauderTarget = null;

	private HashMap<EmulationCharacter, EmulationCharacter> evilKillVotes = new HashMap<>();

	private LinkedList<EmulationCharacter> amnesiacs = new LinkedList<>();

	private LinkedList<EmulationCharacter> deadPlayers = new LinkedList<>();

	private int day;

	public GameEmulator(int day) {
		this.day = day;
	}

	public void block(EmulationCharacter blocker, EmulationCharacter target) {
		blockers.put(blocker, target);
	}

	public void guard(EmulationCharacter guard, EmulationCharacter target) {
		guards.put(guard, target);
	}

	public void voteEvilKill(EmulationCharacter killer, EmulationCharacter target) {
		evilKillVotes.put(killer, target);
	}

	public void forceEvilKill(EmulationCharacter killer, EmulationCharacter target) {
		marauderTarget = target;
		marauder = killer;
	}

	public void kill(EmulationCharacter killer, EmulationCharacter target) {
		killers.put(killer, target);
	}

	public void knightKill(EmulationCharacter knight, EmulationCharacter target) {
		knights.put(knight, target);
	}

	public void sirenKill(EmulationCharacter siren, EmulationCharacter target) {
		sirens.put(siren, target);
		killers.put(siren, target);
	}

	public void heal(EmulationCharacter healer, EmulationCharacter target) {
		healers.put(healer, target);
	}

	public void inquest(EmulationCharacter inquisitor, EmulationCharacter target) {
		inquisitors.put(inquisitor, target);
	}

	public void amnesiac(EmulationCharacter amnesiac) {
		amnesiacs.add(amnesiac);
	}

	public LinkedList<EmulationCharacter> getDeadPlayers() {
		return deadPlayers;
	}

	public void emulate() {
		prepareEvilTargets();
		emulateBlockers();
		emulateKillersGuardsHealers();
		emulateSirens();
		emulateKnights();
		emulateAmnesiacs();
		emulateInquisitors();
	}

	private void prepareEvilTargets() {
		if (marauderTarget == null) {
			HashMap<EmulationCharacter, Integer> votes = new HashMap<>();
			for (EmulationCharacter voter : evilKillVotes.keySet()) {
				EmulationCharacter vote = evilKillVotes.get(voter);
				if (votes.containsKey(vote)) {
					if (votes.get(vote) > Math.floor(evilKillVotes.size() / 2.0)) {
						kill(voter, vote);
						return;
					} else
						votes.put(vote, votes.get(vote) + 1);
				} else
					votes.put(vote, 1);
			}
		} else {
			kill(marauder, marauderTarget);
		}
	}

	private void emulateBlockers() {
		for (EmulationCharacter blocker : blockers.keySet()) {
			EmulationCharacter blocked = blockers.get(blocker);

			if (blocked != null) {
				blocked.addNightMessage("Someone kept you occupied during the night.");
				EmulationCharacter target;

				target = killers.get(blocked);
				if (target != null) {
					killers.remove(blocked);
					break;
				}

				target = healers.get(blocked);
				if (target != null) {
					healers.remove(blocked);
				}

				target = inquisitors.get(blocked);
				if (target != null) {
					inquisitors.remove(blocked);
				}
			}
		}
	}

	private void emulateKillersGuardsHealers() {
		for (EmulationCharacter killer : killers.keySet()) {
			EmulationCharacter killed = killers.get(killer);

			if (killed != null) {
				boolean guarded = false;
				for (EmulationCharacter guard : guards.keySet()) {
					if (guard.getTargetid().equals(killed.getLobbyPlayer().getId()) && !guarded) {
						killed.addNightMessage("Someone attacked you during the night, but a guard saved you.");
						killer.addNightMessage("Someone was guarding your target.");
						guard.addNightMessage("You died while fighting off an attacker.");
						deadPlayers.add(killer);
						deadPlayers.add(guard);
					}
				}
				if (!guarded) {
					boolean healed = false;
					for (EmulationCharacter healer : healers.keySet()) {
						if (healer.getTargetid().equals(killed.getLobbyPlayer().getId())) {
							healed = true;
							killed.addNightMessage("Someone attacked you during the night, but a priest healed you.");
							healer.addNightMessage("Your target was attacked, but you saved them.");
							killer.addNightMessage("Your target was saved from the attack.");
						}
					}
					if (!healed) {
						killed.addNightMessage("Someone attacked you.");
						deadPlayers.add(killed);
					}
				}
			}
		}
	}

	private void emulateSirens() {
		for (EmulationCharacter siren : sirens.keySet()) {
			if (deadPlayers.contains(sirens.get(siren)))
				sirens.get(siren).getLobbyPlayer().setRoleMask(RoleMasks.Drowned);
		}
	}

	private void emulateKnights() {
		for (EmulationCharacter knight : knights.keySet()) {
			EmulationCharacter knightExecution = knights.get(knight);
			if (knightExecution != null) {
				deadPlayers.add(knightExecution);
				if (knightExecution.getRole().getAlignment() == Alignments.Good
						|| knightExecution.getRole().getAlignment() == Alignments.ChaoticGood)
					deadPlayers.add(knight);
			}
		}
	}

	private void emulateInquisitors() {
		for (EmulationCharacter inquisitor : inquisitors.keySet()) {
			EmulationCharacter inquest = inquisitors.get(inquisitor);
			/*
			 * if (inquest != null)
			 * inquisitor.addNightMessage(inquest.getInquestMessage());
			 */
		}
	}

	private void emulateAmnesiacs() {
		for (EmulationCharacter amnesiac : amnesiacs) {
			if (day >= 2) {
				RoleInterface newRole = Alignments.Good.getRandomRoleFromThisAlignment();
				amnesiac.setRole(newRole);
				amnesiac.getLobbyPlayer().setRole(newRole);
				amnesiac.addMessage(new LobbyMessage("role", amnesiac.getLobbyPlayer().getRole().getName(),
						amnesiac.getLobbyPlayer().getRole().getAlignment().getAlignmentName(),
						(String) amnesiac.getLobbyPlayer().getRole().getAlignment().getGoal(),
						amnesiac.getLobbyPlayer().getRole().getDescription()));
				amnesiac.addNightMessage("You just remembered that you're a " + amnesiac.getRole().getName() + ".");
			} else {
				amnesiac.addNightMessage("You struggle to remember who you are.");
			}
		}
	}
}
