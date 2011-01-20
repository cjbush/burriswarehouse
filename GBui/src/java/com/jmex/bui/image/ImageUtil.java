//
// $Id: ImageUtil.java,v 1.4 2007/05/02 21:34:06 vivaldi Exp $
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

package com.jmex.bui.image;

import com.jmex.bui.BImage;

import java.net.URL;

/**
 * @author timo
 * @since 27Apr07
 */
public class ImageUtil {

    public static BImage getImage(final URL img) {
        BImage image = null;

        try {
            image = new BImage(img);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return image;
    }

    public static BImage getImage(final String path,
                                  final String img) {
        return getImage(ImageUtil.class.getResource(path + img));
    }
}
