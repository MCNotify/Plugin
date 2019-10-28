package org.zonex.commands.zx;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.zonex.ZoneX;
import org.zonex.commands.autocomplete.AutoCompleteNode;
import org.zonex.commands.autocomplete.AutoCompleter;
import org.zonex.commands.autocomplete.CommandAutoCompleteNode;
import org.zonex.communication.auth.Response;
import org.zonex.commands.AbstractCommand;
import org.zonex.commands.HelpFactory;
import org.zonex.communication.notifications.*;
import org.zonex.config.Configuration;
import org.zonex.config.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ZxCommandHandler extends AbstractCommand {

    public ZxCommandHandler(){
        super("zx", true);
        AutoCompleter autoCompleter = new AutoCompleter();
        autoCompleter.addNode(new CommandAutoCompleteNode("help"));
        autoCompleter.addNode(new CommandAutoCompleteNode("about"));
        autoCompleter.addNode(new CommandAutoCompleteNode("email"));
        autoCompleter.addNode(new CommandAutoCompleteNode("sms"));
        autoCompleter.addNode(new CommandAutoCompleteNode("discord"));
        autoCompleter.addNode(new CommandAutoCompleteNode("verify").addChild(new CommandAutoCompleteNode("email")).addChild(new CommandAutoCompleteNode("sms")));
        autoCompleter.addNode(new CommandAutoCompleteNode("devices"));

        autoCompleter.addNode(new CommandAutoCompleteNode("area").requiresPermissionNode(Permission.ADMIN));
        autoCompleter.addNode(new CommandAutoCompleteNode("watch").requiresPermissionNode(Permission.ADMIN));
        this.setAutoCompleter(autoCompleter);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length < 1)
            return;

        switch(args[0]){
            case "help":
                new HelpFactory().sendCommandList(sender);
                break;
            case "email":
                if(!Boolean.valueOf(Configuration.EMAIL_ENABLED.getValue())){
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "Email is disabled on this server.");
                    return;
                }

                if(args.length == 2) {
                    String targetEmail = args[1];

                    if (targetEmail.toLowerCase().equals("stop")) {
                        ZoneX.communicationManager.removeCommunicationProtocol((Player) sender, CommunicationProtocol.EMAIL);
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You will no longer receive email notifications.");
                        return;
                    }


                    // Check if the user already subscribes via email.
                    CommunicationMethod method = ZoneX.communicationManager.getCommunicationMethod((Player) sender, CommunicationProtocol.EMAIL);
                    if (method == null || !method.isVerified()) {
                        // Add a new communicaiton method and send their verification code.
                        ZoneX.communicationManager.addCommunicationProtocol((Player) sender, new EmailNotification((Player)sender, args[1]));
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] Notifications will not be sent until you verify your email. A verification code has been sent to your device. Verify with /zx verify email <yourCode>");
                    } else {
                        // Player has already registered for a communication and has already verified it.
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You have already registered an email: " + method.getTarget() + ". Unregister with /zx email stop");
                    }
                }
                break;
            case "sms":
                if(!Boolean.valueOf(Configuration.TWILIO_ENABLED.getValue())){
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "SMS is disabled on this server.");
                    return;
                }

                if(args.length == 2) {
                    String targetEmail = args[1];

                    if (targetEmail.toLowerCase().equals("stop")){
                        ZoneX.communicationManager.removeCommunicationProtocol((Player) sender, CommunicationProtocol.SMS);
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You will no longer receive text notifications.");
                        return;
                    }


                    // Check if the user already subscribes via sms.
                    CommunicationMethod method = ZoneX.communicationManager.getCommunicationMethod((Player) sender, CommunicationProtocol.SMS);
                    if (method == null || !method.isVerified()) {
                        // Add a new communicaiton method and send their verification code.
                        ZoneX.communicationManager.addCommunicationProtocol((Player) sender, new SmsNotification((Player)sender, args[1]));
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] Notifications will not be sent until you verify your email. A verification code has been sent to your device. Verify with /zx verify sms <yourCode>");
                    } else {
                        // Player has already registered for a communication and has already verified it.
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You have already registered a phone number: " + method.getTarget() + ". Unregister with /zx sms stop");
                    }
                }
                break;
            case "discord":
                if(!Boolean.valueOf(Configuration.DISCORD_SRV_ENABLED.getValue())){
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "Discord is disabled on this server.");
                    return;
                }

                if(args.length == 2 && args[1].toLowerCase().equals("stop")){
                    ZoneX.communicationManager.removeCommunicationProtocol((Player) sender, CommunicationProtocol.DISCORD);
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You will no longer receive discord notifications.");
                    return;
                }

                // Check if the user already subscribes via discord.
                CommunicationMethod discordCommunication = ZoneX.communicationManager.getCommunicationMethod((Player) sender, CommunicationProtocol.DISCORD);
                if(discordCommunication == null || !discordCommunication.isVerified()){
                    DiscordNotification discordNotification = new DiscordNotification((Player)sender);
                    ZoneX.communicationManager.addCommunicationProtocol((Player)sender, discordNotification);
                    if(!discordNotification.isVerified()){
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] You have not linked your discord account. Use /discord link <username> to link your account.");
                    } else {
                        if(discordNotification.getTarget() != null) {
                            sender.sendMessage(ChatColor.GREEN + "[ZoneX] Discord successfully linked with " + discordNotification.getTarget() + ".");
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "[ZoneX] Discord successfully linked.");
                        }
                    }
                } else {
                    // Player has already registered for a communication and has already verified it.
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You have already registered your discord: " + discordCommunication.getTarget() + ". Unregister with /zx discord stop");
                }

                break;
            case "verify":
                if(args.length != 3){
                    return;
                }
                CommunicationProtocol verificationType = CommunicationProtocol.valueOf(args[1].toUpperCase());
                String verificationCode = args[2];

                if(verificationType == null){
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + " Accepted arguments are: email, sms");
                    return;
                } else {
                    if(ZoneX.communicationManager.verifyCommunicationProtocol((Player)sender, verificationType, verificationCode)){
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + " Successfully verified. You will now receive " + verificationType.toString() + " notifications.");
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + " Invalid verification code.");
                    }
                }
                break;
            case "devices":
                // Determine all communication protocols.
                ArrayList<CommunicationMethod> methods = ZoneX.communicationManager.getCommunicationMethods((Player)sender);
                if(methods == null){
                    sender.sendMessage(ChatColor.GREEN + "[ZoneX] " + ChatColor.WHITE + "You are not recieving any notifications.");
                } else {
                    sender.sendMessage(ChatColor.GOLD + "======== Communication Devices ========");
                    for(CommunicationMethod method : methods){
                        sender.sendMessage(ChatColor.GREEN + method.getProtocol().toString() + " - " + method.getTarget() + " - " + (method.isVerified() ? "Verified" : "Not Verified"));
                    }
                }
                break;
        }
    }
}
