package com.geeksville.andropilot.gui

import android.os.Bundle
import android.view.View
import scala.collection.JavaConverters._
import android.widget.SimpleAdapter
import org.mavlink.messages.ardupilotmega.msg_rc_channels_raw
import com.geeksville.flight.MsgRcChannelsChanged
import com.geeksville.util.ThreadTools._
import android.support.v4.app.ListFragment
import com.geeksville.andropilot.R
import com.geeksville.andropilot.service._

class RcChannelsFragment extends SimpleListFragment {

  override def onVehicleReceive = {
    case MsgRcChannelsChanged(_) =>
      if (isVisible) {
        //debug("Received Rc channels")
        handler.post(updateList _)
      }
  }

  private def rcToSeq(m: msg_rc_channels_raw) =
    Seq("Channel 1" -> m.chan1_raw, "Channel 2" -> m.chan2_raw, "Channel 3" -> m.chan3_raw, "Channel 4" -> m.chan4_raw,
      "Channel 5" -> m.chan5_raw, "Channel 6" -> m.chan6_raw, "Channel 7" -> m.chan7_raw, "Channel 8" -> m.chan8_raw /* "Rssi" -> m.rssi */ )

  protected def makeAdapter(): Option[SimpleAdapter] = {
    for { v <- myVehicle; rc <- v.rcChannels } yield {
      seqToAdapter(rcToSeq(rc))
    }
  }
}
