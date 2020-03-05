echo "----------------shut down tomcat-------------------"
tomcatId=`ps -ef | grep 'java' | grep 'weixin-java-miniapp-demo' | grep -v 'grep' | awk '{print $2}'`
if [ -n "$tomcatId"];then
echo 'current application not start'
fi
echo "----------------startup tomcat-------------------"
nohup java -jar weixin-java-miniapp-demo-1.0.0-SNAPSHOT.jar >output &
sleep 1
tail -f output
