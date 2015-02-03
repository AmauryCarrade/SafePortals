/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package eu.carrade.amaury.SafePortals;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public final class PortalsListener implements Listener {
	
	/**
	 * Used to move the portal inside the world border
	 * 
	 * @param ev
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerPortal(PlayerPortalEvent ev) {
		
		ev.useTravelAgent(false);
		
		TravelAgent travelAgent = ev.getPortalTravelAgent();
		
		Location destination = travelAgent.findOrCreate(ev.getTo());
		
		// If Bukkit tries to link to a portal out of the border, we forces the
		// generation of a new portal, inside this border.
		// ev.getTo() is already the good location for this portal.
		if(!SafePortalsUtils.isInsideBorder(destination)) {
			Boolean success = travelAgent.createPortal(ev.getTo());

			if(success) {
				ev.setTo(travelAgent.findPortal(ev.getTo()));

				// The portal exact location returned by the portal agent may not be safe.
				if (!SafePortalsUtils.isSafeSpot(ev.getTo())) {
					Location safeTo = SafePortalsUtils.searchSafeSpot(ev.getTo());
					if (safeTo != null) {
						ev.setTo(safeTo);
					}
				}
			}
		}

		else {
			ev.setTo(destination);
		}
	}
}
