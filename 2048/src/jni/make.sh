gcc -I/usr/local/java/include/ -I/usr/local/java/include/linux -fPIC -c shift.c 
gcc -shared -Wl,-soname,libshift.so.1 -o libshift.so.1.0 shift.o
ln -sf libshift.so.1.0 libshift.so.1
ln -sf libshift.so.1 libshift.so
