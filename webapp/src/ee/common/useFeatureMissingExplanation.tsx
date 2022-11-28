import { useTranslate } from '@tolgee/react';
import { LINKS } from 'tg.constants/links';
import { useGlobalContext } from 'tg.globalContext/GlobalContext';

export function useFeatureMissingExplanation() {
  const subscription = useGlobalContext((c) => c.eeSubscription);
  const isAdmin = useGlobalContext(
    (c) => c.userInfo?.globalServerRole === 'ADMIN'
  );

  const { t } = useTranslate();

  function ifAdmin<T>(value: T) {
    if (isAdmin) {
      return value;
    } else {
      return undefined;
    }
  }

  if (subscription && subscription.status === 'ACTIVE') {
    return {
      message: t('feature-explanation-license-not-sufficient'),
      actionTitle: ifAdmin(t('feature-explanation-check-license-action')),
      link: ifAdmin(LINKS.ADMINISTRATION_EE_LICENSE.build()),
    };
  }

  if (subscription && subscription.status !== 'ACTIVE') {
    return {
      message: t('feature-explanation-license-not-active'),
      actionTitle: ifAdmin(t('feature-explanation-check-license-action')),
      link: ifAdmin(LINKS.ADMINISTRATION_EE_LICENSE.build()),
    };
  }

  if (!subscription) {
    return {
      message: t('feature-explanation-no-license'),
      actionTitle: ifAdmin(t('feature-explanation-setup-license')),
      link: ifAdmin(LINKS.ADMINISTRATION_EE_LICENSE.build()),
    };
  }

  return {};
}
