package todo.oauth.domain.repository.client;

import java.util.Set;

import todo.oauth.domain.model.Client;

public interface ClientRepository {
	/** クライアントIDに紐付いているClientを返す */
	public Client findClientByClientId(String clientId);

	/** クライアントIDに紐付いている全てのスコープを返す */
	public Set<String> findClientScopesByClientId(String clientId);

	/** クライアントIDに紐付いている全てのリソースを返す */
	public Set<String> findClientResourcesByClientId(String clientId);

	/** クライアントIDに紐付いている全ての認可グラントを返す */
	public Set<String> findClientGrantsByClientId(String clientId);

	/** クライアントIDに紐付いている全てのリダイレクトURIを返す */
	public Set<String> findClientRedirectUrisByClientId(String clientId);
}