/* File:             $Id$
* Revision:         $Revision$
* Author:           $Author$
* Date:             $Date$
*
* The Netarchive Suite - Software to harvest and preserve websites
* Copyright 2004-2007 Det Kongelige Bibliotek and Statsbiblioteket, Denmark
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package dk.netarkivet.archive.arcrepositoryadmin;

import dk.netarkivet.common.distribute.arcrepository.BitArchiveStoreState;

import java.util.Date;

/** This class contains a storestate, and the time,
  *  when it was last set.
  */
public class ArchiveStoreState {

    /** The state for a specific archive, or overall state. */
    private BitArchiveStoreState storestate;

    /** Time of last state change. */
    private Date lastchanged;

    /**
     * Constructor for this class.
     * This sets the lastchanged value to Now.
     * @param storestate A BitArchiveStoreState
     */
    public ArchiveStoreState(BitArchiveStoreState storestate) {
        setState(storestate);
    }

    /**
     * Constructor for this class.
     * @param storestate A BitArchiveStoreState
     * @param lastchanged Time for when this state was set
     */
    public ArchiveStoreState(BitArchiveStoreState storestate,
            Date lastchanged) {
        setState(storestate, lastchanged);
    }

    /***
     * Return the current BitArchiveStoreState.
     * @return the current BitArchiveStoreState
     */
    public BitArchiveStoreState getState(){
        return storestate;
    }

    /**
     * Sets the current BitarchiveStoreState.
     * @param storestate the BitarchiveStoreState
     * @param lastchanged the lastchanged
     */
    public void setState(BitArchiveStoreState storestate, Date lastchanged) {
        this.storestate = storestate;
        this.lastchanged = lastchanged;
    }

    /**
     * Sets the current BitarchiveStoreState.
     * As a sideeffect sets lastchanged to NOW.
     * @param storestate the BitarchiveStoreState
     */
    public void setState(BitArchiveStoreState storestate) {
        this.storestate = storestate;
        this.lastchanged = new Date();
    }

    /**
     * Get the Date for when the state was lastchanged.
     * @return the Date for when the state was lastchanged
     */
    public Date getLastChanged() {
        return this.lastchanged;

    }

    public String toString() {
        String stringRepresentation = getState() + " "
        + getLastChanged().getTime();
        return stringRepresentation;
    }

}