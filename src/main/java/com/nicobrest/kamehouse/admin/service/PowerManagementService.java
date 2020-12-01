package com.nicobrest.kamehouse.admin.service;

import com.nicobrest.kamehouse.main.exception.KameHouseBadRequestException;
import com.nicobrest.kamehouse.main.exception.KameHouseException;
import com.nicobrest.kamehouse.main.exception.KameHouseServerErrorException;
import com.nicobrest.kamehouse.main.utils.DateUtils;
import com.nicobrest.kamehouse.main.utils.PropertiesUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

/**
 * Service to execute power management commands.
 *
 * @author nbrest
 */
@Service
public class PowerManagementService {

  private static final Logger logger = LoggerFactory.getLogger(PowerManagementService.class);
  private static final int WOL_PORT = 9;
  private static final String TRIGGER_WONT_FIRE = "Based on configured schedule, the given "
      + "trigger will never fire";

  @Autowired
  private Scheduler scheduler;

  @Autowired
  private JobDetail suspendJobDetail;

  /**
   * Wake on lan the specified server. The server should be the base of the the admin.properties
   * [server].mac and [server].broadcast.
   * For example: "media.server"
   */
  public void wakeOnLan(String server) {
    logger.trace("Waking up {}", server);
    String macAddress = PropertiesUtils.getAdminProperty(server + ".mac");
    String broadcastAddress = PropertiesUtils.getAdminProperty(server + ".broadcast");
    if (macAddress == null || broadcastAddress == null) {
      throw new KameHouseBadRequestException("Invalid server specified " + server);
    }
    wakeOnLan(macAddress, broadcastAddress);
  }

  /**
   * Wake on lan the specified MAC address in format FF:FF:FF:FF:FF:FF or FF-FF-FF-FF-FF-FF
   * using the specified broadcast address.
   */
  public void wakeOnLan(String macAddress, String broadcastAddress) {
    try {
      byte[] macAddressBytes = getMacAddressBytes(macAddress);
      byte[] wolPacketBytes = new byte[6 + 16 * macAddressBytes.length];
      for (int i = 0; i < 6; i++) {
        wolPacketBytes[i] = (byte) 0xff;
      }
      for (int i = 6; i < wolPacketBytes.length; i += macAddressBytes.length) {
        System.arraycopy(macAddressBytes, 0, wolPacketBytes, i, macAddressBytes.length);
      }

      InetAddress broadcastInetAddress = InetAddress.getByName(broadcastAddress);
      DatagramPacket wolPacket = new DatagramPacket(wolPacketBytes, wolPacketBytes.length,
          broadcastInetAddress, WOL_PORT);
      DatagramSocket datagramSocket = new DatagramSocket();
      datagramSocket.send(wolPacket);
      datagramSocket.close();
      logger.debug("WOL packet sent to {} on broadcast {}", macAddress, broadcastAddress);
    } catch (IOException e) {
      logger.error("Error sending WOL packet to {}", macAddress, e);
      throw new KameHouseException(e);
    }
  }

  /**
   * Get the mac address as a byte array.
   */
  private static byte[] getMacAddressBytes(String macAddress) throws KameHouseException {
    try {
      byte[] macAddressBytes = new byte[6];
      String[] hex = macAddress.split("(\\:|\\-)");
      if (hex.length != 6) {
        throw new KameHouseBadRequestException("Invalid MAC address " + macAddress);
      }
      for (int i = 0; i < 6; i++) {
        macAddressBytes[i] = (byte) Integer.parseInt(hex[i], 16);
      }
      return macAddressBytes;
    } catch (NumberFormatException e) {
      throw new KameHouseBadRequestException("Invalid MAC address " + macAddress);
    }
  }

  /**
   * Schedule a server suspend at the specified delay in seconds.
   */
  public void scheduleSuspend(Integer delay) {
    try {
      if (delay == null || delay < 0) {
        throw new KameHouseBadRequestException("Invalid delay specified");
      }
      Trigger suspendTrigger = getSuspendTrigger(delay);
      if (scheduler.checkExists(suspendTrigger.getKey())) {
        scheduler.rescheduleJob(suspendTrigger.getKey(), suspendTrigger);
      } else {
        scheduler.scheduleJob(suspendTrigger);
      }
      logger.debug("Scheduling suspend with a delay of {} seconds", delay);
    } catch (SchedulerException e) {
      if (e.getMessage() != null && e.getMessage().contains(TRIGGER_WONT_FIRE)) {
        logger.debug(e.getMessage());
      } else {
        throw new KameHouseServerErrorException(e.getMessage(), e);
      }
    }
  }

  /**
   * Get current suspend status.
   */
  public String getSuspendStatus() {
    try {
      Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey("suspendTrigger"));
      if (trigger != null && trigger.getNextFireTime() != null) {
        return "Suspend scheduled at: " + trigger.getNextFireTime().toString();
      } else {
        return "Suspend not scheduled";
      }
    } catch (SchedulerException e) {
      throw new KameHouseServerErrorException(e.getMessage(), e);
    }
  }

  /**
   * Cancel a current scheduled suspend.
   */
  public String cancelScheduledSuspend() {
    try {
      boolean cancelledSuspend = scheduler.unscheduleJob(TriggerKey.triggerKey("suspendTrigger"));
      if (cancelledSuspend) {
        return "Suspend cancelled";
      } else {
        return "Suspend was not scheduled, so no need to cancel";
      }
    } catch (SchedulerException e) {
      throw new KameHouseServerErrorException(e.getMessage(), e);
    }
  }

  /**
   * Get the trigger to schedule the job to suspend the server at the specified delay in seconds.
   */
  private Trigger getSuspendTrigger(int delay) {
    Date currentDate = new Date();
    Date scheduleDate = DateUtils.addSeconds(currentDate, delay);
    String cronExpression = DateUtils.toCronExpression(scheduleDate);
    ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
    return TriggerBuilder.newTrigger()
        .forJob(suspendJobDetail)
        .withIdentity(TriggerKey.triggerKey("suspendTrigger"))
        .withDescription("Trigger to schedule a server suspend")
        .withSchedule(scheduleBuilder)
        .build();
  }
}
