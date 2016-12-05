package shared;


/**
* shared/FSInterfaceHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from frontEnd/Interface.idl
* Sunday, December 4, 2016 11:05:13 PM EST
*/

abstract public class FSInterfaceHelper
{
  private static String  _id = "IDL:shared/FSInterface:1.0";

  public static void insert (org.omg.CORBA.Any a, shared.FSInterface that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static shared.FSInterface extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (shared.FSInterfaceHelper.id (), "FSInterface");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static shared.FSInterface read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_FSInterfaceStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, shared.FSInterface value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static shared.FSInterface narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof shared.FSInterface)
      return (shared.FSInterface)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      shared._FSInterfaceStub stub = new shared._FSInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static shared.FSInterface unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof shared.FSInterface)
      return (shared.FSInterface)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      shared._FSInterfaceStub stub = new shared._FSInterfaceStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
