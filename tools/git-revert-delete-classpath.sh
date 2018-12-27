for D in *; do
    if [ -d "${D}" ]; then
        echo "${D}"
        git checkout  78ae8b78dbb99a16edaa458ff5ea76dfe3fca2af 	"${D}/.classpath"
    fi
done
