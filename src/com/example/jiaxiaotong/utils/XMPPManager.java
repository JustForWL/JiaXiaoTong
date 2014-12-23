package com.example.jiaxiaotong.utils;

import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import com.example.jiaxiaotong.constants.App;

/**
 * connect to server 
 * @author arthur
 *
 */
public class XMPPManager {
	private static XMPPManager instance = null;
    private XMPPConnection connection=null;
    private static String SUCCESS="SUCCESS";
    private XMPPManager( ){
        /**
        Manages providers for parsing custom XML sub-documents of XMPP packets. Two types of providers exist:
        IQProvider -- parses IQ requests into Java objects.
        PacketExtension -- parses XML sub-documents attached to packets into PacketExtension instances.
         **/
    	init();
    }

    /**
     * 单例方法
     * @return
     */
    public static XMPPManager getInstance() {
        if (instance == null) {
            instance = new XMPPManager();
        }
        return instance;
    }

    /**
     * 获得connection连接
     * @return
     */
    public XMPPConnection getConnection() throws Exception {

        if (connection == null)
            throw new Exception("请先初始化xmppconnection");
            return connection;

    }
    /**
     *
     * 登陆操作  返回String 来判断登陆结果
     Code XMPP Error Type
     500 interna-server-error WAIT
     403 forbidden AUTH
     400bad-request MODIFY
     404 item-not-found CANCEL
     409 conflict CANCEL
     501 feature-not-implemented CANCEL
     302 gone MODIFY
     400 jid-malformed MODIFY
     406 no-acceptable MODIFY
     405 not-allowed CANCEL
     401 not-authorized AUTH
     402 payment-required AUTH
     404 recipient-unavailable WAIT
     302 redirect MODIFY
     407 registration-required AUTH
     404 remote-server-not-found CANCEL
     504 remote-server-timeout WAIT
     502 remote-server-error CANCEL
     500 resource-constraint WAIT
     503 service-unavailable CANCEL
     407 subscription-required AUTH
     500 undefined-condition WAIT
     400 unexpected-condition WAIT
     408 request-timeout CANCEL
     *
     * @param username
     * @param password
     * 
     * 
     */
    public String isLogin(String username, String password) {
        try {
            connection.connect();
            connection.login(username, password);
            return SUCCESS;
        } catch (XMPPException e) {
        	e.printStackTrace();
        	Logger.i("login server error" + e.getMessage());
            return e.getMessage();
        }
    }
    
    private void init() {
    	ProviderManager pm = ProviderManager.getInstance();
    	configure(pm);
    	ConnectionConfiguration cf = new ConnectionConfiguration(
    			App.HOST,
    			App.PORT,
    			App.DEVICE);
    	//cf.setDebuggerEnabled(true);  //开启debug模式
    	cf.setSendPresence(true);
    	cf.setCompressionEnabled(false);  //是否对流进行压缩
    	cf.setSASLAuthenticationEnabled(false); //是否开启SASL 登陆验证
    	connection = new XMPPConnection(cf);
    }
    public void configure(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
        }

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",
                new RosterExchangeProvider());
        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event",
                new MessageEventProvider());
        // Chat State
        pm.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());
        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());

    }

    /**
     * 返回组信息的容器
     * @param roster
     * @return
     */
    public Iterator<RosterGroup> getGroups(Roster roster) {
        Collection<RosterGroup> rosterGroups = roster.getGroups();
        return rosterGroups.iterator();
    }

}
