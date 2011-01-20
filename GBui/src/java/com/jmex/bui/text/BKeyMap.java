//
// $Id: BKeyMap.java,v 1.2 2007/04/27 19:46:34 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005, Michael Bayne, All Rights Reserved
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.jmex.bui.text;

/**
 * Maps key presses with specific modifier combinations to editor commands. These are used by the text-entry
 * components.
 */
public class BKeyMap {
    /**
     * A command constant indicating no mapping exists for a particular modifier and key code combination.
     */
    public static final int NO_MAPPING = -1;

    /**
     * A modifiers code that if specified, will default any keyCode to the specified command unless a specific modifier
     * mapping is set.
     */
    public static final int ANY_MODIFIER = -1;

    /**
     * Adds a mapping for the specified modifier and key code combination to the specified command.
     */
    public void addMapping(int modifiers,
                           int keyCode,
                           int command) {
        int kidx = keyCode % BUCKETS;

        // override any preexisting mapping
        for (Mapping map = _mappings[kidx]; map != null; map = map.next) {
            if (map.matches(modifiers, keyCode)) {
                map.command = command;
                return;
            }
        }

        // create a new mapping
        Mapping map = new Mapping(modifiers, keyCode, command);
        map.next = _mappings[kidx];
        _mappings[kidx] = map;
    }

    /**
     * Looks up and returns the command associated with the specified set of modifiers and key code. Returns {@link
     * #NO_MAPPING} if no matching mapping can be found.
     */
    public int lookupMapping(int modifiers,
                             int keyCode) {
        int kidx = keyCode % BUCKETS;
        int defaultCommand = NO_MAPPING;
        for (Mapping map = _mappings[kidx]; map != null; map = map.next) {
            if (map.matches(modifiers, keyCode)) {
                return map.command;
            } else if (map.matches(ANY_MODIFIER, keyCode)) {
                defaultCommand = map.command;
            }
        }
        return defaultCommand;
    }

    /**
     * Contains information about a single key mapping.
     */
    protected static class Mapping {
        public int modifiers;
        public int keyCode;
        public int command;
        public Mapping next;

        public Mapping(int modifiers,
                       int keyCode,
                       int command) {
            this.modifiers = modifiers;
            this.keyCode = keyCode;
            this.command = command;
        }

        public boolean matches(int modifiers,
                               int keyCode) {
            return (modifiers == this.modifiers && keyCode == this.keyCode);
        }
    }

    /**
     * Contains a primitive hashmap of mappings.
     */
    protected Mapping[] _mappings = new Mapping[BUCKETS];

    /**
     * The number of mapping buckets we maintain.
     */
    protected static final int BUCKETS = 64;
}
