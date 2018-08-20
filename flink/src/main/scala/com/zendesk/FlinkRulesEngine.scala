package com.zendesk

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import com.zendesk.utils.Globals._
import com.zendesk.model._
import com.zendesk.protocol.Protocol._
import com.zendesk.validation.ModelRuleValidation._
import org.apache.flink.runtime.state.memory.MemoryStateBackend


object FlinkRulesEngine {
  def main(args: Array[String]): Unit = {
    val backEndtore = new MemoryStateBackend(MAX_MEM_STATE_SIZE, true);
    val env: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    // val text = env.socketTextStream(zenCfg.getString("host.name"), zenCfg.getInt("host.port"))
    val text = env.readTextFile("file:///Users/kayvan/dev/workspaces/workspace-fp/rules-engine-kayvan/inputs/data.json")
    val zenStream = text
      .map( x => asZenModel(x))
      .filter(_.isValid).map(x ⇒ x.toOption.get)

    zenStream.print

    env.execute("FlinkRulesEngine")
  }
}
