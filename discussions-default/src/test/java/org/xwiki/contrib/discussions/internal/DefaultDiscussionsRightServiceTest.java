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
package org.xwiki.contrib.discussions.internal;

import java.util.Date;

import javax.inject.Provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.xwiki.contrib.discussions.domain.Discussion;
import org.xwiki.contrib.discussions.domain.Message;
import org.xwiki.contrib.discussions.internal.rights.AdminDiscussionRight;
import org.xwiki.contrib.discussions.internal.rights.ReadDiscussionRight;
import org.xwiki.contrib.discussions.internal.rights.WriteDiscussionRight;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.model.reference.WikiReference;
import org.xwiki.security.authorization.AuthorizationManager;
import org.xwiki.security.authorization.Right;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;
import org.xwiki.test.junit5.mockito.MockComponent;

import com.xpn.xwiki.XWikiContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test of {@link DefaultDiscussionsRightService}.
 *
 * @version $Id$
 * @since 1.0
 */
@ComponentTest
class DefaultDiscussionsRightServiceTest
{
    public static final DocumentReference USER_DOCUMENT_REFERENCE = new DocumentReference("xwiki", "XWiki", "User");

    private static final DocumentReference DISCUSSION_REFERENCE = new DocumentReference("xwiki", "XWiki", "Discussion");

    public static final Discussion DISCUSSION =
        new Discussion("referenceDiscussion", "title", "description", new Date());

    public static final String USER_REFERENCE = "xwiki:XWiki.User";

    @InjectMockComponents
    private DefaultDiscussionsRightService defaultDiscussionsRightService;

    @MockComponent
    private AuthorizationManager authorizationManager;

    @MockComponent
    private Provider<XWikiContext> xcontextProvider;

    @MockComponent
    private EntityReferenceSerializer<String> entityReferenceSerializer;

    @Mock
    private XWikiContext xWikiContext;

    @Mock
    private WikiReference wikiReference;

    @Mock
    private Right adminDiscussionRight;

    @Mock
    private Right readDiscussionRight;

    @Mock
    private Right writeDiscussionRight;

    @BeforeEach
    void setUp() throws Exception
    {
        when(this.xcontextProvider.get()).thenReturn(this.xWikiContext);
        when(this.xWikiContext.getUserReference()).thenReturn(USER_DOCUMENT_REFERENCE);
        when(this.xWikiContext.getWikiReference()).thenReturn(this.wikiReference);

        when(this.authorizationManager.register(AdminDiscussionRight.INSTANCE)).thenReturn(this.adminDiscussionRight);
        when(this.authorizationManager.register(ReadDiscussionRight.INSTANCE)).thenReturn(this.readDiscussionRight);
        when(this.authorizationManager.register(WriteDiscussionRight.INSTANCE)).thenReturn(this.writeDiscussionRight);

        when(this.entityReferenceSerializer.serialize(USER_DOCUMENT_REFERENCE)).thenReturn(USER_REFERENCE);

        this.defaultDiscussionsRightService.initialize();
    }

    @Test
    void canCreateDiscussionDisallowed()
    {
        setUserAdminStatus(false);
        boolean b = this.defaultDiscussionsRightService.canCreateDiscussion();
        assertFalse(b);
    }

    private void setUserAdminStatus(boolean b2)
    {
        when(this.authorizationManager.hasAccess(Right.ADMIN, USER_DOCUMENT_REFERENCE, this.wikiReference))
            .thenReturn(b2);
    }

    @Test
    void canCreateDiscussionAllowed()
    {
        setUserAdminStatus(true);
        boolean b = this.defaultDiscussionsRightService.canCreateDiscussion();
        assertTrue(b);
    }

    @Test
    void canCreateDiscussionContextDisallowed()
    {
        setUserAdminStatus(false);
        boolean b = this.defaultDiscussionsRightService.canCreateDiscussionContext();
        assertFalse(b);
    }

    @Test
    void canCreateDiscussionContextAllowed()
    {
        setUserAdminStatus(true);
        boolean b = this.defaultDiscussionsRightService.canCreateDiscussionContext();
        assertTrue(b);
    }

    @Test
    void canReadDiscussionDisallowed()
    {
        when(this.authorizationManager
            .hasAccess(this.readDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE))
            .thenReturn(false);
        boolean b = this.defaultDiscussionsRightService.canReadDiscussion(DISCUSSION_REFERENCE);
        assertFalse(b);
    }

    @Test
    void canReadDiscussionAllowed()
    {
        when(this.authorizationManager
            .hasAccess(this.readDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE))
            .thenReturn(true);
        boolean b = this.defaultDiscussionsRightService.canReadDiscussion(DISCUSSION_REFERENCE);
        assertTrue(b);
    }

    @Test
    void canWriteDiscussionDisallowed()
    {
        when(this.authorizationManager
            .hasAccess(this.writeDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE))
            .thenReturn(false);
        boolean b = this.defaultDiscussionsRightService.canWriteDiscussion(DISCUSSION_REFERENCE);
        assertFalse(b);
    }

    @Test
    void canWriteDiscussionAllowed()
    {
        when(this.authorizationManager
            .hasAccess(this.writeDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE))
            .thenReturn(true);
        boolean b = this.defaultDiscussionsRightService.canWriteDiscussion(DISCUSSION_REFERENCE);
        assertTrue(b);
    }

    @Test
    void canWriteDiscussionContextDisallowed()
    {
        when(this.authorizationManager
            .hasAccess(this.writeDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE))
            .thenReturn(false);
        boolean b = this.defaultDiscussionsRightService.canWriteDiscussionContext(DISCUSSION_REFERENCE);
        assertFalse(b);
    }

    @Test
    void canWriteDiscussionContextAllowed()
    {
        when(this.authorizationManager
            .hasAccess(this.writeDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE))
            .thenReturn(true);
        boolean b = this.defaultDiscussionsRightService.canWriteDiscussionContext(DISCUSSION_REFERENCE);
        assertTrue(b);
    }

    @Test
    void canDeleteMessageNotLocalUser()
    {
        Message message = new Message("reference", "content", "no_local", "actorReference", new Date(), new Date(),
            DISCUSSION);
        boolean b = this.defaultDiscussionsRightService.canDeleteMessage(message, DISCUSSION_REFERENCE);
        assertFalse(b);
    }

    @Test
    void canDeleteMessageNotAuthor()
    {
        Message message = new Message("reference", "content", "user", "actorReference", new Date(), new Date(),
            DISCUSSION);
        boolean b = this.defaultDiscussionsRightService.canDeleteMessage(message, DISCUSSION_REFERENCE);
        assertFalse(b);
    }

    @Test
    void canDeleteMessageIsAuthorButWriteDisallowed()
    {
        Message message = new Message("reference", "content", "user", "xwiki:XWiki.User", new Date(), new Date(),
            DISCUSSION);
        boolean b = this.defaultDiscussionsRightService.canDeleteMessage(message, DISCUSSION_REFERENCE);
        assertFalse(b);
    }

    @Test
    void canDeleteMessageIsAuthorAndWriteAllowed()
    {
        Message message = new Message("reference", "content", "user", "xwiki:XWiki.User", new Date(), new Date(),
            DISCUSSION);
        when(this.authorizationManager
            .hasAccess(this.writeDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE)).thenReturn(true);
        boolean b = this.defaultDiscussionsRightService.canDeleteMessage(message, DISCUSSION_REFERENCE);
        assertTrue(b);
    }

    @Test
    void canDeleteMessageNotAuthorButAdmin()
    {
        Message message = new Message("reference", "content", "user", "actorReference", new Date(), new Date(),
            DISCUSSION);
        when(this.authorizationManager
            .hasAccess(this.adminDiscussionRight, USER_DOCUMENT_REFERENCE, DISCUSSION_REFERENCE)).thenReturn(true);
        boolean b = this.defaultDiscussionsRightService.canDeleteMessage(message, DISCUSSION_REFERENCE);
        assertTrue(b);
    }

    @Test
    void isAdminDiscussion()
    {
        // TODO
    }

    @Test
    void isAdministrator()
    {
        // TODO
    }
}