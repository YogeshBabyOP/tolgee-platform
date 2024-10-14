import {FunctionComponent, useEffect} from 'react';
import {useHistory, useRouteMatch} from 'react-router-dom';

import {LINKS, PARAMS} from 'tg.constants/links';

import {useGlobalContext} from 'tg.globalContext/GlobalContext';
import {FullPageLoading} from 'tg.component/common/FullPageLoading';
import {useSsoService} from 'tg.component/security/SsoService';

interface SsoRedirectionHandlerProps {}
const LOCAL_STORAGE_STATE_KEY = 'oauth2State';

export const SsoRedirectionHandler: FunctionComponent<
  SsoRedirectionHandlerProps
> = () => {
  const allowPrivate = useGlobalContext((c) => c.auth.allowPrivate);

  const { loginWithOAuthCodeOpenId } = useSsoService();
  const match = useRouteMatch();
  const history = useHistory();

  useEffect(() => {
    const searchParam = new URLSearchParams(window.location.search);
    const code = searchParam.get('code');
    const state = searchParam.get('state');
    const storedState = localStorage.getItem(LOCAL_STORAGE_STATE_KEY);
    if (storedState !== state) {
      history.replace(LINKS.LOGIN.build());
    } else {
      localStorage.removeItem(LOCAL_STORAGE_STATE_KEY);
    }

    if (code && !allowPrivate) {
      loginWithOAuthCodeOpenId(match.params[PARAMS.SERVICE_TYPE], code);
    }
  }, [allowPrivate]);

  return <FullPageLoading />;
};
