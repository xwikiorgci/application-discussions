/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.discussions.internal.rights;

import java.util.Set;

import org.xwiki.model.EntityType;
import org.xwiki.security.authorization.Right;
import org.xwiki.security.authorization.RightDescription;
import org.xwiki.security.authorization.RuleState;

import static org.xwiki.security.authorization.RuleState.DENY;

/**
 * A programmatic right for Admin discussion feature.
 *
 * @version $Id$
 * @since 1.0
 */
public final class AdminDiscussionRight implements RightDescription
{
    /**
     * Singleton instance for a admin discussion right.
     */
    public static final AdminDiscussionRight INSTANCE = new AdminDiscussionRight();

    private AdminDiscussionRight()
    {
    }

    @Override
    public String getName()
    {
        return "Admin Discussion";
    }

    @Override
    public RuleState getDefaultState()
    {
        return DENY;
    }

    @Override
    public RuleState getTieResolutionPolicy()
    {
        return DENY;
    }

    @Override
    public boolean getInheritanceOverridePolicy()
    {
        return false;
    }

    @Override
    public Set<Right> getImpliedRights()
    {
        return null;
    }

    @Override
    public Set<EntityType> getTargetedEntityType()
    {
        return Right.WIKI_SPACE_DOCUMENT;
    }

    @Override
    public boolean isReadOnly()
    {
        return false;
    }
}
