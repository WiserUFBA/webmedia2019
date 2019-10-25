Iniciar o Minishift

sudo ./minishift start

Conectando a instancia OpenShift

oc login https://192.168.42.36:8443 -u developer -p developer

Criando o projeto
oc new-project reactive-microservices-exemplo-webmedia

Selecionado o projeto
oc project nome_projeto

Fornecendo permissões ao usuário developer
oc policy add-role-to-user admin developer -n reactive-microservices-circuit-breaker

oc policy add-role-to-user view -n reactive-microservices-circuit-breaker -z default

Acessando informações sobre um serviço

oc describe routes nome_servico


