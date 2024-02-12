package io.tolgee.service.slackIntegration

import io.tolgee.dtos.request.slack.SlackCommandDto
import io.tolgee.exceptions.NotFoundException
import io.tolgee.model.Project
import io.tolgee.model.UserAccount
import io.tolgee.model.slackIntegration.SlackConfig
import io.tolgee.repository.slackIntegration.SlackConfigRepository
import io.tolgee.service.automations.AutomationService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SlackConfigService(
  private val automationService: AutomationService,
  private val slackConfigRepository: SlackConfigRepository
) {

  fun get(
    projectId: Long,
    channelId: String,
  ): SlackConfig {
    return slackConfigRepository.findByProjectIdAndChannelId(projectId, channelId)
      ?: throw NotFoundException()
  }

  fun get(
    configId: Long,
  ): SlackConfig {
    return slackConfigRepository.findById(configId).orElseThrow { NotFoundException() }
  }

  @Transactional
  fun delete(
    projectId: Long,
    channelId: String
  ) {
    val config = get(projectId, channelId)
    automationService.deleteForSlackIntegration(config)
    slackConfigRepository.delete(config)
  }

  fun create(project: Project, payload: SlackCommandDto, user: UserAccount): SlackConfig {
    val slackConfig = SlackConfig(project, user)
    slackConfig.channelId = payload.channel_id

    slackConfigRepository.save(slackConfig)
    automationService.createForSlackIntegration(slackConfig)
    return slackConfig
  }

}
