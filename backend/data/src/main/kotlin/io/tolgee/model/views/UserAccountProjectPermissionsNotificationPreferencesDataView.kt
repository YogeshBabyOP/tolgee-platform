/**
 * Copyright (C) 2023 Tolgee s.r.o. and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.tolgee.model.views

import io.tolgee.model.enums.OrganizationRoleType
import io.tolgee.model.enums.ProjectPermissionType
import io.tolgee.model.enums.Scope
import io.tolgee.model.notifications.NotificationPreferences

class UserAccountProjectPermissionsNotificationPreferencesDataView(
  val id: Long,
  val projectId: Long,
  val organizationRole: OrganizationRoleType?,
  val basePermissionsBasic: ProjectPermissionType?,
  basePermissionsGranular: Array<Enum<Scope>>?,
  val permissionsBasic: ProjectPermissionType?,
  permissionsGranular: Array<Enum<Scope>>?,
  permittedViewLanguages: String?,
  globalNotificationPreferences: NotificationPreferences?,
  projectNotificationPreferences: NotificationPreferences?
) {
  // My love for Hibernate have no limit 🥰🥰🥰
  val basePermissionsGranular = basePermissionsGranular?.map { enumValueOf<Scope>(it.name) }
  val permissionsGranular = permissionsGranular?.map { enumValueOf<Scope>(it.name) }
  val permittedViewLanguages = permittedViewLanguages?.let {
    it.split(',')
      .filter { part -> part.isNotEmpty() }
      .map { part -> part.toLong() }
      .ifEmpty { null }
  }

  val notificationPreferences = projectNotificationPreferences ?: globalNotificationPreferences
}
